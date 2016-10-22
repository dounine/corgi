package com.dounine.corgi.rpc.spring;

import com.dounine.corgi.cluster.Balance;
import com.dounine.corgi.register.api.Register;
import com.dounine.corgi.rpc.protocol.IProtocol;

/**
 * Created by huanghuanlai on 2016/10/18.
 */
public interface IRpc {

    Register getRegister();

    Balance getBalance();

    IProtocol getProtocol();

}
