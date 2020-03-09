package com.example.jpaexample.controller;

import com.example.jpaexample.service.RabbitMqProducerService;
import com.philips.services.iot.logger.Logger;
import com.philips.services.iot.logger.models.LogParamBuilder;
import com.philips.services.iothub.commoncomponents.httpabstraction.application.HttpAbstractionFactoryImpl;
import com.philips.services.iothub.commoncomponents.httpabstraction.models.*;
import com.philips.services.iothub.commoncomponents.httpabstraction.service.HttpAbstraction;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public class ClientApplication {

    @Autowired
    private HttpAbstractionFactoryImpl HttpAbstractionFactoryProducerImpl;

    @Autowired
    private OperationResultBuilder operationResultBuilder;

    @Value("${uri}")
    private String uri;

    @Autowired
    private ProxyDetails proxyDetails;
    @Autowired
    private RequestAttributes requestAttributes;
    @Autowired
    private Context context;

    @Autowired
    RabbitMqProducerService rabbitMqProducerService;

    public HttpRequest getHttpRequest(String uri){
        return HttpRequest.newBuilder().uri(URI.create(uri))
                .GET().build();
    }


    public String sendRequestToWeb(){
        HttpAbstraction httpAbstraction = HttpAbstractionFactoryProducerImpl.getHttpAbstractionInstance();
        OperationResult operationResult = null;
        HttpRequest httpRequest=getHttpRequest(uri);
        HttpResponse<String> httpResponse = null;
        setRequestParams();
        try {
            httpResponse = httpAbstraction.sendRequest(httpRequest, proxyDetails, context,
                    requestAttributes);
            operationResult = operationResultBuilder.buildOperationResult(httpResponse, context);
            if (operationResult.getStatusCode() == HttpStatusCode.SUCCESS) {
                rabbitMqProducerService.mqProducer(operationResult.getHttpResponseBody());
            }
            else{
                System.out.println("Did not receive response from web");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return operationResult.getHttpResponseBody();
    }

    public void setRequestParams(){
        //Logger.traceMethodStart(new LogParamBuilder().correlationId(CSMConstants.CORRELATION_ID).build());
        proxyDetails.setProxyPort("");
        proxyDetails.setProxyAddress("");
        List<Integer> httpcodesRetryList = new ArrayList<Integer>();
        httpcodesRetryList.add(HttpStatusCode.INTERNAL_SERVER_ERROR);
        requestAttributes.setHttpcodesRetryList(httpcodesRetryList);
        requestAttributes.setMaxNumOfRetries(3);
        requestAttributes.setSuccessHttpStatusCode(HttpStatusCode.SUCCESS);
        requestAttributes.setConnectionTimeOut("10000");
        context.setContextType("Fetching data");
        context.setSubContextType("Data from web");
        //Logger.traceMethodExit(new LogParamBuilder().correlationId(CSMConstants.CORRELATION_ID).build());
    }
}
