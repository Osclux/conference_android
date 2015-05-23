package com.example.luxxor.konferensplattan;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import java.util.Collections;

/**
 * Created by luxxor on 2015-05-23.
 */
public class TokViewListener implements View.OnClickListener {
    View rootView;
    LinearLayout linearLayout;
    TextView ipadress;
    String port = "4567";
    String url;
    TextView output;


    @Override
    public void onClick(View v) {
        this.linearLayout = (LinearLayout)rootView.findViewById(R.id.activesessions);
        this.ipadress = (TextView)rootView.findViewById(R.id.ipadress);
        this.url  = "http://"+ipadress.getText().toString()+":"+port+"/activesessions";
        this.output =  (TextView)rootView.findViewById(R.id.outputText);
        output.setText("Waiting for answer ...");
        linearLayout.removeAllViews();
        GsonRequest activeSessionsRequest = new GsonRequest(url, ActiveSessionList.class, Collections.<String, String>emptyMap(),
                new Response.Listener<ActiveSessionList>() {
                    @Override
                    public void onResponse(final ActiveSessionList response) {
                        /*ImageView imgView = new ImageView(rootView.getContext());
                        imgView.setImageResource(R.drawable.video);
                        imgView.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
                        imgView.setLayoutParams(new Gallery.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        linearLayout.addView(imgView);*/
                        output.setText(response.toString());
                        final ActiveSession aktivSession = response.list.get(0);
                        final Session mSession = new Session(rootView.getContext(), aktivSession.apiKey,aktivSession.sessionId);
                        mSession.setSessionListener(new Session.SessionListener() {
                            @Override
                            public void onConnected(Session session) {
                               output.setText("Connected to session " + aktivSession.name);
                                Publisher mPublisher = new Publisher(rootView.getContext(), "Android-video");
                                session.publish(mPublisher);

                            }

                            @Override
                            public void onDisconnected(Session session) {

                            }

                            @Override
                            public void onStreamReceived(Session session, Stream stream) {
                                final Subscriber mSubscriber = new Subscriber(rootView.getContext(), stream);
                                mSubscriber.setSubscriberListener(new SubscriberKit.SubscriberListener() {
                                    @Override
                                    public void onConnected(SubscriberKit subscriberKit) {
                                        linearLayout.addView(mSubscriber.getView());
                                    }

                                    @Override
                                    public void onDisconnected(SubscriberKit subscriberKit) {

                                    }

                                    @Override
                                    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
                                        output.setText("ERROR2: " + opentokError.getMessage());
                                    }
                                });
                                session.subscribe(mSubscriber);
                            }

                            @Override
                            public void onStreamDropped(Session session, Stream stream) {

                            }

                            @Override
                            public void onError(Session session, OpentokError opentokError) {
                                output.setText("ERROR1: " + opentokError.getMessage());
                            }
                        });
                        mSession.connect(aktivSession.token);

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                output.setText("Error" + error.getMessage());

            }

        });
        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
        queue.add(activeSessionsRequest);
    }

    public TokViewListener(View view) {
        this.rootView = view;


    }

}
