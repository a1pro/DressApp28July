package com.dressapplication.ui.chat;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.media.Image;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Image> mImage;

    public ChatViewModel() {
        mText = new MutableLiveData<>();
      /*  mImage=new MutableLiveData<>();
       // mText.setValue("This is dashboard fragment");
        mImage.setValue(mImage.getValue());*/
    }

    public LiveData<String> getText() {
        return mText;
    }

}