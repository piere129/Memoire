package com.example.pieter.memoire.ClickListeners;

public interface ButtonClickListener {

    /**
     * Interface allowing the button clicked in
     * the view of ThemesRecyclerView to be implemented in Adapter,
     * while also calling its' onActivityResult method.
     *
     * @param position
     */
    public void startIntentToCards(int position);
}
