package com.mcp.assignment.model;

public class MCPResponse {

    private String jsonrpc = "2.0";
    private Object result;
    private MCPError error;
    private Object id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public MCPError getError() {
        return error;
    }

    public void setError(MCPError error) {
        this.error = error;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}