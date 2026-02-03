package com.longfei.yang.study.bean;

/**
 * 工具配置实体类
 */
public class ToolConfig {
    private Long id;
    private String toolName;
    private String methodName;
    private String description;
    private String inputSchema;
    private Boolean enabled;
    private String targetClass;

    public ToolConfig() {
    }

    public ToolConfig(Long id, String toolName, String methodName, String description, 
                      String inputSchema, Boolean enabled, String targetClass) {
        this.id = id;
        this.toolName = toolName;
        this.methodName = methodName;
        this.description = description;
        this.inputSchema = inputSchema;
        this.enabled = enabled;
        this.targetClass = targetClass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(String inputSchema) {
        this.inputSchema = inputSchema;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }
}
