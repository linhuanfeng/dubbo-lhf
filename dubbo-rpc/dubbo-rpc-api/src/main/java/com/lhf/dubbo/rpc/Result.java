package com.lhf.dubbo.rpc;


import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Deprecated
public interface Result {
    <U> CompletableFuture<U> thenApply(Function<Result,? extends U> fn);
}
