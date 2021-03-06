package zephyr.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class EchoServer {

    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    // 测试命令：telnet localhost 8080
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new EchoServer(port).run();
    }

    public void run() throws Exception {
        // NioEventLoopGroup 是一个处理I/O操作的多线程事件循环

        // 定义acceptor (接收连接)
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 定义client (处理客户端连接后的时间)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建ServerBootstrap的实例以引导和绑定服务器
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 指定channel实例类型
                    .channel(NioServerSocketChannel.class)
                    // 定义客户端连接后的 事件处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    })
                    // 添加额外配置
                    .option(ChannelOption.SO_BACKLOG, 128) // 临时存放已完成三次握手的请求的队列的最大长度
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // 是否启用心跳保活机制

            // 异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind(port).sync();

            // 阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
