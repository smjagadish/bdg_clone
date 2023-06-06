package com.baeldung.grpc.sample;

import io.grpc.Context;

public class Constants {
    // context keys are based on reference equality check
    // extremely important to ensure the same reference is used during setting/getting
    // the 'name' of the key doesnt really matter . 2 keys with same name are anyways different ibject references
    // this constant is set in the  server interceptor and retrieved in another server interceptor , server service impl
    static final Context.Key<String> auth_token = Context.key("auth_token");
    // this constant is set in the server service call and retrieved in the resp interceptor and also set as trailer MD
    static final Context.Key<String> resp_token = Context.key("resp_token");
}
