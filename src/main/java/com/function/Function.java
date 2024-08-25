package com.function;

import com.domain.Authentication;
import com.domain.UpdateQueueMapping;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.ExecutionContext;

import java.util.Optional;

public class Function {

    private final UpdateQueueMapping updateQueueMapping;
    private final Authentication authentication;

    public Function() {
        this.updateQueueMapping = new UpdateQueueMapping();
        this.authentication = new Authentication();
    }

    @FunctionName("updateQueueMappings")
    public HttpResponseMessage updateQueueMappings(
            @HttpTrigger(name = "req", methods = {HttpMethod.PUT}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<UpdateRequest>> request,
            ExecutionContext context) {

        String authHeader = request.getHeaders().get("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Unauthorized access").build();
        }

        String[] credentials = extractCredentials(authHeader);
        if (credentials == null || !authentication.authenticate(credentials[0], credentials[1])) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Invalid credentials").build();
        }

        UpdateRequest updateRequest = request.getBody().orElse(null);
        if (updateRequest == null || !updateRequest.isValid()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Invalid request data").build();
        }

        boolean success = updateQueueMapping.updateMapping(updateRequest.getPublisherId(), 
                                                           updateRequest.getOldConsumerQueueName(), 
                                                           updateRequest.getOldEventType(), 
                                                           updateRequest.getNewConsumerQueueName(), 
                                                           updateRequest.getNewEventType());
        return success ? request.createResponseBuilder(HttpStatus.OK).body("Mapping updated successfully").build() 
                       : request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update mapping").build();
    }

    private String[] extractCredentials(String authHeader) {
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
        final String[] values = credentials.split(":", 2);
        return values.length == 2 ? values : null;
    }
}
