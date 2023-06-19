package com.snc.zero.fragment.listener;

import androidx.fragment.app.Fragment;

/**
 * Fragment Interaction
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public interface MessageListener {

    void onMessage(Fragment fragment, String command, Object[] params);

}

