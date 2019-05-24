package com.example.swethababurao.boseweatherchannel.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swethababurao.boseweatherchannel.MainActivity;
import com.example.swethababurao.boseweatherchannel.R;

public class RecyclerViewFragment extends Fragment {


    public RecyclerViewFragment() {
    }

    /** onAttach(), onCreate(),  onCreateView(),  onActivityCreated(),    onStart(), onResume() **/
    /** onDetach(), onDestroy(), onDestroyView()(recreate from backstack),onStop(),  onPause()  **/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        Bundle bundle = this.getArguments();
        View view = inflater.inflate(R.layout.three_hourly_weather_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MainActivity mainActivity = (MainActivity) getActivity();
        recyclerView.setAdapter(mainActivity.getAdapter(bundle.getInt("day")));
        return view;
    }

}