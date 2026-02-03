package com.longfei.yang.study.tools;

import com.longfei.yang.study.bean.ToolConfig;
import com.longfei.yang.study.repository.ToolConfigRepository;
import com.longfei.yang.study.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToolsUtil {
    private static final Logger logger = LoggerFactory.getLogger(ToolsUtil.class);

    @Autowired
    private ToolConfigRepository toolConfigRepository;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 从数据库动态加载工具配置并构建 ToolCallback 列表
     *
     * @return 工具回调列表
     */

    public List<ToolCallback> getToolCallList() {
        List<ToolCallback> toolCallbacks = new ArrayList<>();

        try {
            // 1. 从数据库中读取所有启用的工具配置
            List<ToolConfig> toolConfigs = toolConfigRepository.findAllEnabled();

            if (toolConfigs == null || toolConfigs.isEmpty()) {
                logger.warn("未找到启用的工具配置，返回空列表");
                return toolCallbacks;
            }

            // 2. 遍历配置，动态构建 ToolCallback
            for (ToolConfig config : toolConfigs) {
                try {
                    // 2.1 获取目标类
                    Class<?> targetClass = Class.forName(config.getTargetClass());

                    // 2.2 从 Spring 容器中获取目标对象实例
                    Object targetObject = applicationContext.getBean(targetClass);

                    // 2.3 通过反射获取方法（这里假设方法参数都是 String 类型，实际可以更灵活）
                    Method method = findMethodByName(targetClass, config.getMethodName());

                    if (method == null) {
                        logger.error("未找到方法: {} 在类 {}", config.getMethodName(), config.getTargetClass());
                        continue;
                    }

                    // 2.4 构建 ToolDefinition
                    ToolDefinition toolDefinition = ToolDefinition.builder()
                            .name(config.getToolName())
                            .description(config.getDescription())
                            .inputSchema(config.getInputSchema())
                            .build();

                    // 2.5 构建 ToolCallback
                    ToolCallback toolCallback = MethodToolCallback.builder()
                            .toolDefinition(toolDefinition)
                            .toolMethod(method)
                            .toolObject(targetObject)
                            .build();

                    toolCallbacks.add(toolCallback);
                    logger.info("成功加载工具: {}", config.getToolName());

                } catch (ClassNotFoundException e) {
                    logger.error("未找到目标类: {}", config.getTargetClass(), e);
                } catch (Exception e) {
                    logger.error("加载工具配置失败: {}", config.getToolName(), e);
                }
            }

        } catch (Exception e) {
            logger.error("从数据库加载工具配置时发生错误", e);
        }

        return toolCallbacks;
    }

    /**
     * 根据方法名查找方法（支持多参数）
     */
    private Method findMethodByName(Class<?> clazz, String methodName) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }


    public List<ToolCallback> getToolCallList(ToolsUtil ticketTools) {

        // TODO 1. 从数据库中读取工具列表
        // 这里可以从数据库查询工具配置，例如：
        // List<ToolConfig> toolConfigs = toolConfigRepository.findAllEnabled();
        // 然后根据配置动态构建 ToolCallback 列表
        
        // 当前实现：基于反射动态构建工具列表
        List<ToolCallback> toolCallbacks = new java.util.ArrayList<>();
        
        // 获取 cancel 方法
        Method cancelMethod = ReflectionUtils.findMethod(ToolsUtil.class, "cancel", String.class, String.class);
        if (cancelMethod != null) {
            ToolDefinition cancelToolDef = ToolDefinition.builder()
                    .name("cancel")
                    .description("退票")
                    .inputSchema("""
                            {
                              "type": "object",
                              "properties": {
                                "ticketNumber": {
                                  "type": "string",
                                  "description": "预定号，可以是纯数字"
                                },
                                "name": {
                                  "type": "string",
                                  "description": "真实人名（必填，必须为人的真实姓名，严禁用其他信息代替；如缺失请传null）"
                                }
                              },
                              "required": ["ticketNumber", "name"]
                            }
                            """)
                    .build();
            
            ToolCallback cancelToolCallback = MethodToolCallback.builder()
                    .toolDefinition(cancelToolDef)
                    .toolMethod(cancelMethod)
                    .toolObject(ticketTools)
                    .build();
            
            toolCallbacks.add(cancelToolCallback);
        }
        
        // 可以继续添加其他工具方法
        // 例如：查询票据、预订票据等
        
        return toolCallbacks;
    }




}
