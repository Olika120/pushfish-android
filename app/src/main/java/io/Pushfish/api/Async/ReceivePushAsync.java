package io.Pushfish.api.Async;

import android.os.AsyncTask;

import io.Pushfish.api.DatabaseHandler;
import io.Pushfish.api.PushListAdapter;
import io.Pushfish.api.PushfishApi.PushfishApi;
import io.Pushfish.api.PushfishApi.PushfishException;
import io.Pushfish.api.PushfishApi.PushfishMessage;

import java.util.ArrayList;
import java.util.Arrays;


public class ReceivePushAsync extends AsyncTask<Void, Void, ArrayList<PushfishMessage>> {
    private PushfishApi api;
    private PushListAdapter adapter;
    private PushfishException error;
    private ReceivePushCallback callback;

    public ReceivePushAsync(PushfishApi api, PushListAdapter adapter) {
        this.api = api;
        this.adapter = adapter;
    }

    public void setCallBack(ReceivePushCallback cb) {
        this.callback = cb;
    }

    @Override
    protected ArrayList<PushfishMessage> doInBackground(Void... voids) {
        try {
            return new ArrayList<PushfishMessage>(Arrays.asList(this.api.getNewMessage()));
        } catch (PushfishException e) {
            this.error = e;
            return new ArrayList<PushfishMessage>();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<PushfishMessage> result) {
        DatabaseHandler dbh = new DatabaseHandler(this.api.getContext());
        for (PushfishMessage msg : result)
            dbh.addMessage(msg);
        adapter.addEntries(result);
        if (this.callback != null) {
            this.callback.receivePush(result);
        }
    }
}
