package com.datadigger.datainsight.exception;

public enum DataDiggerErrorCode {
	CONNECTION_INFO_PROVIDER_NOT_FOUND,
	CONNECTION_INFO_PROVIDER_RETURN_NULL,
	DATABASE_NOT_SUPPORT_CHARSET,
	DECLARE_METHOD_ERROR,
	HIBERNATE_EXCEPTION,
	METHOD_NAME_ERROR,
	NULL_POINTER_ERROR,
	PARAM_COUNT_ERROR,
	PARAM_TYPE_ERROR,
	SESSION_INVALID,
	UNKOWN_ERROR,
	XML_ERROR,
	LICENSE_ERROR, 
	LICENSE_EXCEED_LIMIT,
	LICENSE_EXPIRED,
	OBJECT_NOT_FOUND_ERROR,
	SQL_ERROR,
	OBJECT_POOL_ERROR,
	;
	
	private static final MessageHelper helper = MessageHelper.getInstance(DataDiggerErrorCode.class);

	public String toString() {
		return helper.getMessage(this.name());
	}
}
