package com.bokecc.common.socket;

import com.bokecc.common.socket.emitter.Emitter;

public interface Listener extends Emitter.Listener {

    public void call(Object... args);

}
