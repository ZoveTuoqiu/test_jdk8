package thrift.impl;

import org.apache.thrift.TException;
import thrift.HelloWorldService;

/**
 * Created by gaoziye on 2016/7/22.
 */
public class HelloWorldImpl implements HelloWorldService.Iface {

    public HelloWorldImpl() {
    }

    @Override
    public String sayHello(String username) throws TException {
        return "Hi," + username + " welcome .";
    }

}
