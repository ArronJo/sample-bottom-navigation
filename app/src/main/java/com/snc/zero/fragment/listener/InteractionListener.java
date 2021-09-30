package com.snc.zero.fragment.listener;

import androidx.fragment.app.Fragment;

/**
 * Fragment Interaction
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public interface InteractionListener {

    void onInteraction(Fragment fragment, String command, String[] params);

}

