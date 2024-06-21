package com.vnptit.ic.sample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vnptit.ic.sanmple.databinding.ActivityLogResultBinding;
import com.vnptit.idg.sdk.utils.KeyResultConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      final ActivityLogResultBinding binding = ActivityLogResultBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      final Intent intent = getIntent();

      ArrayList<LogResult> logResults;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
         logResults = intent.getParcelableArrayListExtra(SampleActivity.EXTRA_LOG_RESULT, LogResult.class);
      } else {
         logResults = intent.getParcelableArrayListExtra(SampleActivity.EXTRA_LOG_RESULT);
      }

      binding.close.setOnClickListener(v -> finish());

      binding.copiedAll.setOnClickListener(v -> {
         copy(intent.getStringExtra(KeyResultConstants.INFO_RESULT));
         copy(intent.getStringExtra(KeyResultConstants.LIVENESS_CARD_FRONT_RESULT));
         copy(intent.getStringExtra(KeyResultConstants.LIVENESS_CARD_REAR_RESULT));
         copy(intent.getStringExtra(KeyResultConstants.COMPARE_RESULT));
         copy(intent.getStringExtra(KeyResultConstants.LIVENESS_FACE_RESULT));
         copy(intent.getStringExtra(KeyResultConstants.MASKED_FACE_RESULT));
      });

      binding.content.removeAllViews();
      if (logResults != null) {
         for (LogResult result : logResults) {
            final LogResultView resultView = new LogResultView(this);
            resultView.setTitle(result.getTitle());
            setLogResultSafe(resultView, result.getResult());
            binding.content.addView(resultView);
         }
      }
   }

   private void copy(final String data) {
      if (!TextUtils.isEmpty(data)) {
         ShareUtils.copyToClipboard(LogActivity.this, data);
      }
   }

   private void setLogResultSafe(final LogResultView view, final String logResult) {
      view.setVisibility(View.VISIBLE);
      if (!TextUtils.isEmpty(logResult)) {
         view.setLogResult(prettify(logResult));
      } else {
         view.setVisibility(View.GONE);
      }
   }

   private String prettify(String json) {
      try {
         switch (detectJsonRoot(json)) {
            case 0:
               return new JSONObject(json).toString(4);
            case 1:
               return new JSONArray(json).toString(4);
            default:
               return json;
         }
      } catch (JSONException e) {
         return json;
      }
   }

   private int detectJsonRoot(String json) {
      try {
         final Object tokener = new JSONTokener(json).nextValue();
         if (tokener instanceof JSONObject) {
            return 0;
         }

         if (tokener instanceof JSONArray) {
            return 1;
         }

         return 2;
      } catch (Exception e) {
         return 2;
      }
   }
}