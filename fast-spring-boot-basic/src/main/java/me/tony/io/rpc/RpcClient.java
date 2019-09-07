package me.tony.io.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 输入描述.
 *
 * @author : weibo
 * @version : v1.0
 * @since : 2019/8/14 16:33
 */
public class RpcClient {

    /**
     * 引用服务.
     *
     * @param interfaceClass 接口类
     * @param host           服务主机
     * @param port           服务断开
     * @param <T>            接口泛型
     * @return 远程服务
     */
    public static <T> T refer(final Class<T> interfaceClass, final String host, final int port) {

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // build rpc request.
                RpcRequest request = new RpcRequest();
                request.setClz(interfaceClass);
                request.setMethodName(method.getName());
                request.setParamType(method.getParameterTypes());
                request.setArgs(args);

                // send request.
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
                RpcClientHandler rpcClientHandler = new RpcClientHandler();
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(eventLoopGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new ObjectEncoder());
                                ch.pipeline().addLast(
                                    new ObjectDecoder(
                                        1024 * 1024,
                                        ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())
                                    )
                                );
                                ch.pipeline().addLast(rpcClientHandler);
                            }
                        })
                    ;
                    ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                    channelFuture.channel().writeAndFlush(request);
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    eventLoopGroup.shutdownGracefully();
                }
                return rpcClientHandler.getResponse();
            }
        });
    }

    public static class RpcClientHandler extends SimpleChannelInboundHandler<Object> {

        private Object response;

        public Object getResponse() {
            return response;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("debug ............ channel read ......... client");
            this.response = msg;
        }
    }

    public static void main(String[] args) {
        HelloWorldSevice helloWorldSevice = RpcClient.refer(HelloWorldSevice.class, "10.88.122.187", 9999);
        System.out.println(helloWorldSevice.sayHello("hello netty rpc.."));
    }

}
