package com.datadigger.datainsight.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@SuppressWarnings("unchecked")
public class DataDiggerException extends RuntimeException implements java.io.Serializable {
	private static final long serialVersionUID = -7643808918438424154L;
	private Enum errorCode;
	private Throwable t;
	private String detail;

	private static Enum getErrorCode(Enum errorCode, Throwable t) {
		Enum code = null;
		if (t instanceof DataDiggerException)
			code = ((DataDiggerException) t).getErrorCode();
		else
			code = errorCode;
		return code;
	}

	public DataDiggerException(Enum errorCode) {
		super(errorCode.toString());
		this.errorCode = errorCode;
	}

	public DataDiggerException(Enum errorCode, Throwable t) {
		super(getErrorCode(errorCode, t).toString(), t);
		if (t instanceof DataDiggerException) {
			this.errorCode = ((DataDiggerException) t).getErrorCode();
			this.t = ((DataDiggerException) t).getCause();
			this.detail = ((DataDiggerException) t).detail;
			if (this.t == null)
				this.t = t;
		} else {
			this.errorCode = errorCode;
			this.t = t;
		}
		if (this.t != null) {
			StackTraceElement[] newTrace = new StackTraceElement[3];
			newTrace[0] = this.getStackTrace()[0];
			newTrace[1] = new StackTraceElement(".", ".", "...", -1);
			for (StackTraceElement item : this.t.getStackTrace()) {
				if (item.getClassName().startsWith("com.datadigger")) {
					newTrace[2] = item;
					break;
				}
			}
			if (newTrace[2] != null)
				this.setStackTrace(newTrace);
		}
	}

	public DataDiggerException setDetail(String detail) {
		this.detail = detail;
		return this;
	}

	public String getDetail() {
		return detail;
	}

	public Enum getErrorCode() {
		return errorCode;
	}

	public String getStackTraceInfo() {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bao);
		printStackTrace(ps);
		ps.close();
		return bao.toString();
	}

	public String getMsg() {
		return errorCode.toString();
	}

	public String toString() {
		String msg = getMsg();
		if (detail != null)
			msg += ":" + detail;
		return msg;
	}
}