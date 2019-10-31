package pl.drobinki;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import pl.drobinki.api.CreateNeedRequest;
import pl.drobinki.domain.Need;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpTrigger-Java". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTrigger-Java&code={your function key}
     * 2. curl "{your host}/api/HttpTrigger-Java?name=HTTP%20Query&code={your function key}"
     * Function Key is not needed when running locally, it is used to invoke function deployed to Azure.
     * More details: https://aka.ms/functions_authorization_keys
     */
    @FunctionName("create-need")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<CreateNeedRequest>> request,
            @CosmosDBOutput(name = "items",
                    databaseName = "ToDoList",
                    collectionName = "Items",
                    connectionStringSetting = "AccountEndpoint=https://658885af-0ee0-4-231-b9ee.documents.azure.com:443/;AccountKey=Orww5H9CEJSaZwJ10X365DbI5aWMo1VAmmfAcSMmd9zr9pmJyM05kytWIYec83UTpdHAES54k7zAajfTK9Z6cA==;")
                    OutputBinding<String> outputItem,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String name = request.getBody().map(req -> req.getName()).orElse(null);


        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            outputItem.setValue(name);
            return request.createResponseBuilder(HttpStatus.OK).body("Hello2, " + name).build();
        }
    }
}
