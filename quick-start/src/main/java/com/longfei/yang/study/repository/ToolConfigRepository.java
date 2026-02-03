package com.longfei.yang.study.repository;

import com.longfei.yang.study.bean.ToolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ToolConfigRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<ToolConfig> TOOL_CONFIG_ROW_MAPPER = new RowMapper<ToolConfig>() {
        @Override
        public ToolConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
            ToolConfig config = new ToolConfig();
            config.setId(rs.getLong("id"));
            config.setToolName(rs.getString("tool_name"));
            config.setMethodName(rs.getString("method_name"));
            config.setDescription(rs.getString("description"));
            config.setInputSchema(rs.getString("input_schema"));
            config.setEnabled(rs.getBoolean("enabled"));
            config.setTargetClass(rs.getString("target_class"));
            return config;
        }
    };

    /**
     * 查询所有启用的工具配置
     */
    public List<ToolConfig> findAllEnabled() {
        String sql = "SELECT * FROM ai_tool_config WHERE enabled = true ORDER BY id";
        return jdbcTemplate.query(sql, TOOL_CONFIG_ROW_MAPPER);
    }

    /**
     * 根据工具名称查询配置
     */
    public ToolConfig findByToolName(String toolName) {
        String sql = "SELECT * FROM ai_tool_config WHERE tool_name = ? AND enabled = true";
        List<ToolConfig> results = jdbcTemplate.query(sql, TOOL_CONFIG_ROW_MAPPER, toolName);
        return results.isEmpty() ? null : results.get(0);
    }
}
