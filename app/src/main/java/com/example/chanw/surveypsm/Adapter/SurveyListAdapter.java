package com.example.chanw.surveypsm.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.chanw.surveypsm.Model.Survey;
import com.example.chanw.surveypsm.R;

import java.util.ArrayList;

/**
 * Created by chanw on 3/26/2017.
 */

public class SurveyListAdapter extends ArrayAdapter<Survey> {

    private static final String TAG = SurveyListAdapter.class.getSimpleName();
    private ArrayList<Survey> mResource;
    Context mContext;

    private static class ViewHolder {
        TextView surveyID;
        TextView surveyName;
        TextView surveyDesc;
        TextView surveyExpiryDate;
    }

    public SurveyListAdapter(@NonNull Context context, ArrayList<Survey> resource) {
        super(context, R.layout.survey_item,resource);
        this.mResource = resource;
        this.mContext = context;

        Log.e(TAG, "Survey List Constructed");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Survey survey = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.survey_item, null);

            viewHolder.surveyID = (TextView) convertView.findViewById(R.id.tv_survey_id);
            viewHolder.surveyName = (TextView) convertView.findViewById(R.id.tv_survey_name);
            viewHolder.surveyDesc = (TextView) convertView.findViewById(R.id.tv_survey_description);
            viewHolder.surveyExpiryDate = (TextView) convertView.findViewById(R.id.tv_expiry_date);

            convertView.setTag(viewHolder);
            result = convertView;

            Log.e(TAG, "Result generated");

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        Log.e(TAG, "B: Set text to viewHolder");
        Log.e(TAG, survey.getId()+ " "+survey.getDescription()+ " "+survey.getSurveyName());
        viewHolder.surveyID.setText(Integer.toString(survey.getId()));
        viewHolder.surveyName.setText(survey.getSurveyName());
        viewHolder.surveyDesc.setText(survey.getDescription());
        viewHolder.surveyExpiryDate.setText(mContext.getString(R.string.expiry_date_string)+" "+survey.getExpiryDate().toString());

        Log.e(TAG, "Set text to viewHolder");

        return result;
    }
}
