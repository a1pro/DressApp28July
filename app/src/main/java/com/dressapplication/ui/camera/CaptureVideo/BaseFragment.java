package com.dressapplication.ui.camera.CaptureVideo;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

public abstract class BaseFragment extends Fragment{
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    protected abstract void setUp(View view);
}
