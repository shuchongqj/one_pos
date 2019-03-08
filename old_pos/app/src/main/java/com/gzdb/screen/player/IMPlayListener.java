
package com.gzdb.screen.player;

/**
 * Description:
 */
public interface IMPlayListener {

    void onStart(IMPlayer player);
    void onPause(IMPlayer player);
    void onResume(IMPlayer player);
    void onComplete(IMPlayer player);

}
