package com.bokecc.common.socket;

import java.util.Arrays;

import com.bokecc.common.utils.Tools;
import com.bokecc.common.socket.client.IO;
import com.bokecc.common.socket.client.Socket;
import com.bokecc.common.socket.emitter.Emitter;
import com.bokecc.common.socket.client.Ack;

/**
 * socket管理类
 * 与业务无关，负责socket调度
 *
 */
public abstract class CCBaseSocket {

    private final String TAG = "CCBaseSocket";

    /**socket*/
    private Socket mSocket;
    /**重连次数*/
    private int mReconnectCount = 0;

    /**重连次数*/
    private final int reconnectionAttempts = 5;


    public CCBaseSocket() {}

    /**
     * 初始化
     * @param url
     */
    public void init(final String url) {
        if(url == null || url.length() == 0){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    release();
                    IO.Options options = new IO.Options();
                    options.forceNew = true;
                    options.reconnection = true;
                    options.reconnectionAttempts = reconnectionAttempts;
                    options.reconnectionDelay = 1000;
                    options.reconnectionDelayMax = 2000;

                    mSocket = IO.socket(url, options);
                    mSocket.connect();
                    bindBaseEvent();
                    bindInteractEvent();
                    mReconnectCount = 0;
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 绑定基础事件
     * 连接
     * 断连
     * 连接超时
     * 连接错误
     * 重连
     * 重连成功
     * 重连错误
     * 重连失败
     */
    private void bindBaseEvent() {
        mSocket.once(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Tools.log(TAG,"connect--EVENT_CONNECT");
                mReconnectCount = 0;
//                mErrorCount = 0;
                onConnet();
            }
        });
        mSocket.on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Tools.log(TAG,"EVENT_CONNECTING");
                onConnecting();
            }
        });
        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                onDisconnect();
                Tools.log(TAG,"disconnect ---- EVENT_DISCONNECT" + Arrays.toString(args));
            }
        });
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Tools.log(TAG,"timeout ---- EVENT_CONNECT_TIMEOUT" + Arrays.toString(args));
                onConnectTimeout();
            }
        });
        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Tools.log(TAG,"error ----EVENT_CONNECT_ERROR " + Arrays.toString(args));
                onConnectError();
            }
        });
        mSocket.on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Tools.log(TAG,"onReconnecting ----EVENT_RECONNECTING "+mReconnectCount);
                onReconnecting();
                mReconnectCount++;
            }
        });
        mSocket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mReconnectCount = 0;
                Tools.log(TAG,"onReconnect ---- EVENT_RECONNECT");
                onReconnect();
            }
        });
        mSocket.on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Tools.log(TAG,"onReconnect error ---- EVENT_RECONNECT_ERROR");
                onReConnectError();
            }
        });
        mSocket.on(Socket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Tools.log(TAG,"onReconnect failed ---- mReconnectCount:"+mReconnectCount);
                onReconnectFailed();
            }
        });
        //重试企图
        mSocket.on(Socket.EVENT_RECONNECT_ATTEMPT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Tools.log(TAG,"EVENT_RECONNECT_ATTEMPT");
                onReconnectAttempt();
            }
        });
        //
        mSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Tools.log(TAG,"EVENT_ERROR");
                onError();
            }
        });
    }


    /**连接成功*/
    protected abstract void onConnet();
    /**连接中*/
    protected abstract void onConnecting();
    /**连接超时*/
    protected abstract void onConnectTimeout();
    /**连接错误*/
    protected abstract void onConnectError();
    /**连接断开*/
    protected abstract void onDisconnect();

    /**重新连接中*/
    protected abstract void onReconnecting();
    /**重新连接成功*/
    protected abstract void onReconnect();
    /**重新连接错误*/
    protected abstract void onReConnectError();
    /**重连失败*/
    protected abstract void onReconnectFailed();
    /**重试尝试*/
    protected abstract void onReconnectAttempt();
    /**连接错误*/
    protected abstract void onError();


    /**绑定自定义事件*/
    protected abstract void bindInteractEvent();

    /**
     * emit事件
     * @param event
     * @param args
     */
    protected void emit(String event, Object... args) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit(event, args);
        } else {
            Tools.log(TAG,"pusher offline please wait...");
        }
    }

    /**
     * emit事件
     *
     * @param event
     * @param args
     */
    protected void emit(String event, final CCSocketCallback ccimackCallback, Object... args) {
        try{
            if (mSocket != null && mSocket.connected()) {
                mSocket.emit(event, args, new Ack() {
                    @Override
                    public void call(Object... objects) {
                        Tools.log(TAG,"emit-Ack-call");
                        ccimackCallback.onResponse(objects);
                    }
                });
            } else {
                Tools.log(TAG, "im pusher offline please wait...");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 日志分发
     * @param event
     * @param fn
     */
    protected void on(String event, Emitter.Listener fn) {
        mSocket.on(event, fn);
    }

    /**
     * 设置所有事件监听
     * @param allEventListener
     */
    protected void setAllEventListener(Emitter.AllEventListener allEventListener){
        mSocket.setAllEventListener(allEventListener);
    }

    /**
     * 关闭socket连接
     */
    public void release() {
        if (mSocket != null) {
            mSocket.disconnect(); // 内部执行为异步执行，Socket.this.destroy();会导致资源释放
        }
        if (mSocket != null) {
            mSocket.off(); // 所以此处需要再次判空
        }
        mSocket = null;
        Tools.log(TAG,"release");
    }

}