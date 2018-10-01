package iiot.sample.domain.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    CREATE_SUCCESS(HttpStatus.OK, "Watch stuff works."),
    NO_SUCH_WATCH(HttpStatus.BAD_REQUEST, "No Such Watch"),
    INVALID_WATCH_ID(HttpStatus.BAD_REQUEST, "Watch Id Provided is Invalid"),
    ENTITY_TYPE_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, "Entity type with this name does not exist"),
    ENTITY_TYPE_IS_REQUIRED(HttpStatus.BAD_REQUEST, "Entity type is required"),
    TRIGGER_IS_INVALID(HttpStatus.BAD_REQUEST, "Trigger is invalid"),
    INVALID_SIMPLE_WATCH_INPUT(HttpStatus.BAD_REQUEST, "Simple Watch Input Provided is Invalid"),
    INVALID_WATCHER_UUID(HttpStatus.BAD_REQUEST, "Watcher Id Provided is Invalid"),
    INVALID_SIMPLE_WATCH_UUID(HttpStatus.BAD_REQUEST, "Simple Watch Id Provided is Invalid"),
    WATCHER_IS_NOT_SUBSCRIBED(HttpStatus.NO_CONTENT, "Watcher is Not Subscribed To Given Simple Watch"),
    WATCHER_IS_ALREADY_SUBSCRIBED(HttpStatus.BAD_REQUEST, "Watcher is Already Subscribed To Given Simple Watch"),
    SIMPLE_WATCH_ALREADY_EXISTS(HttpStatus.CONFLICT, "Simple Watch already exists."),
    SIMPLE_WATCH_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, "Simple Watch does not exist"),
    NOTIFICATION_TEMPLATE_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "Notification template could not be found."),
    WATCH_PREFERENCES_REQUIRED(HttpStatus.BAD_REQUEST, "watch Preferences if provided must have a valid WatchPreferences structure"),
    SERVICE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "Contact support."),
    BAD_REQUEST_BODY(HttpStatus.BAD_REQUEST, "The request body provided in invalid"),
    BAD_REQUEST_BODY_PROPERTIES(HttpStatus.BAD_REQUEST, "The request body provided contains an unrecognized structure"),
    NO_TRANSLATIONS_FOUND(HttpStatus.BAD_REQUEST, "No translations found"),
    ASSET_HIERARCHY_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create assetHierarchy for the given asset"),

    //permissions related to setup

    PERM_MISSING_PERMISSIONS(HttpStatus.FORBIDDEN, "No permissions were found"),
    MISSING_PERM_CONFIG(HttpStatus.INTERNAL_SERVER_ERROR, "No permissions config found for entityName"),
    PERM_NO_ACCESS_TO_VIEW_ENTITY_ON_ASSET(HttpStatus.UNAUTHORIZED, "No access to view entity on asset"),

    // asset validation
    ASSET_IS_REQUIRED(HttpStatus.BAD_REQUEST, "assetUuid associated with the entity beign watched is required"),
    ASSET_ACCESS_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "could not access asset"),
    INPUT_TOO_LONG(HttpStatus.BAD_REQUEST, "too long input"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "invalid or badly formatted input"),



    //watcherTypeValidation
    WATCHER_TYPE_IS_REQUIRED(HttpStatus.UNAUTHORIZED, "WatcherType required")
    ;


    private final HttpStatus httpStatus;
    private final String defaultMessage;



    ErrorCode(HttpStatus httpStatus, String defaultMessage){
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

}
