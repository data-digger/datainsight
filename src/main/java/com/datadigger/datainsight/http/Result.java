package com.datadigger.datainsight.http;


public class Result<T> {

    private boolean success = true;

    private T content;

    public Result(T content) {
        this.content = content;
    }

    public Result(boolean success, T content) {
        this(content);
        this.success = success;
    }



    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
