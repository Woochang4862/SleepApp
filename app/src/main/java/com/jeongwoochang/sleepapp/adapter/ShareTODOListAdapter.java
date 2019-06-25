package com.jeongwoochang.sleepapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jeongwoochang.sleepapp.API.APIClient;
import com.jeongwoochang.sleepapp.API.APIInterface;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.adapter.viewholder.ShareTODOHolder;
import com.jeongwoochang.sleepapp.util.data.Board;
import com.jeongwoochang.sleepapp.util.data.BoardRes;
import com.jeongwoochang.sleepapp.util.data.LoginRes;
import com.jeongwoochang.sleepapp.util.data.UserRes;
import com.jeongwoochang.sleepapp.util.helper.data.SharedPreferencesHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;
import org.joda.time.Interval;
import org.joda.time.Period;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class ShareTODOListAdapter extends BaseRecyclerViewAdapter<ShareTODOHolder> {

    private ArrayList<Board> items;
    private Context context;
    private SharedPreferencesHelper pref;
    private APIInterface service;

    public ShareTODOListAdapter(Context context) {
        this.context = context;
        pref = new SharedPreferencesHelper(context);
        service = APIClient.getClient(context).create(APIInterface.class);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_board_list, parent, false);
        vh = new ShareTODOHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ShareTODOHolder) {
            final Picasso picasso;
            OkHttpClient client = new OkHttpClient();
            picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(client)).build();
            final Board item = items.get(position);
            final KProgressHUD pd = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Loading...")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            service.getUser(item.getId()).enqueue(new retrofit2.Callback<UserRes>() {
                @Override
                public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                    UserRes data = response.body();
                    ((ShareTODOHolder) holder).username.setText(data.getUsername());
                    Timber.d(data.getPhoto());
                    picasso.load("https://good-night-image-bucket.s3.ap-northeast-2.amazonaws.com/" + data.getPhoto()).into(((ShareTODOHolder) holder).profileImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            pd.dismiss();
                        }

                        @Override
                        public void onError(Exception e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void onFailure(Call<UserRes> call, Throwable t) {
                    pd.dismiss();
                }
            });
            pd.show();
            picasso
                    .load("https://good-night-board-image-bucket.s3.ap-northeast-2.amazonaws.com/" + item.getPhoto())
                    .into(((ShareTODOHolder) holder).boardImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Timber.d("Success");
                            pd.dismiss();
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                            pd.dismiss();
                        }
                    });
            ((ShareTODOHolder) holder).title.setText(item.getTitle());
            ((ShareTODOHolder) holder).username.setText(item.getId());
            ((ShareTODOHolder) holder).content.setText(item.getBody_text());
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                ((ShareTODOHolder) holder).boardDate.setText(getSummaryPeriod(sdf.parse(item.getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
                ((ShareTODOHolder) holder).boardDate.setText("날짜 형식 오류");
            }
            final ArrayList<String> likes = item.getLikes();
            if (likes != null) {
                ((ShareTODOHolder) holder).like.setText(String.valueOf(likes.size()));
            }
            final boolean likeIsClicked = likes.contains(new SharedPreferencesHelper(context).getEmail());
            if (likeIsClicked) {
                ((ShareTODOHolder) holder).unlikeBtn.setVisibility(View.VISIBLE);
                ((ShareTODOHolder) holder).likeBtn.setVisibility(View.GONE);
            } else {
                ((ShareTODOHolder) holder).unlikeBtn.setVisibility(View.GONE);
                ((ShareTODOHolder) holder).likeBtn.setVisibility(View.VISIBLE);
            }
            ((ShareTODOHolder) holder).likeArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final APIInterface service = APIClient.getClient(context).create(APIInterface.class);
                    service.getBoards().enqueue(new retrofit2.Callback<BoardRes>() {
                        @Override
                        public void onResponse(Call<BoardRes> call, Response<BoardRes> response) {
                            if (response.body().isStatus()) {
                                if (response.body().getData().get(position).getLikes().contains(pref.getEmail())) { // 종아요 눌려 있음 => 해제
                                    service.unclickLike(String.valueOf(item.getNo())).enqueue(new retrofit2.Callback<LoginRes>() {
                                        @Override
                                        public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                                            if (response.body().isStatus()) {
                                                Toast.makeText(context, "좋아요가 반영되었습니다.", Toast.LENGTH_SHORT).show();
                                                ((ShareTODOHolder) holder).like.setText(String.valueOf((Integer.valueOf(((ShareTODOHolder) holder).like.getText().toString()) - 1)));
                                                ((ShareTODOHolder) holder).unlikeBtn.setVisibility(View.GONE);
                                                ((ShareTODOHolder) holder).likeBtn.setVisibility(View.VISIBLE);
                                            } else {
                                                Toast.makeText(context, "좋아요가 반영되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<LoginRes> call, Throwable t) {

                                        }
                                    });
                                } else { // 종아요 눌려 있지 않음 => 누름
                                    service.clickLike(String.valueOf(item.getNo())).enqueue(new retrofit2.Callback<LoginRes>() {
                                        @Override
                                        public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                                            if (response.body().isStatus()) {
                                                Toast.makeText(context, "좋아요가 반영되었습니다.", Toast.LENGTH_SHORT).show();
                                                ((ShareTODOHolder) holder).like.setText(String.valueOf((Integer.valueOf(((ShareTODOHolder) holder).like.getText().toString()) + 1)));
                                                ((ShareTODOHolder) holder).unlikeBtn.setVisibility(View.VISIBLE);
                                                ((ShareTODOHolder) holder).likeBtn.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(context, "좋아요가 반영되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<LoginRes> call, Throwable t) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BoardRes> call, Throwable t) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        ShareTODOHolder.onItemClickListener = onItemClickListener;
    }

    public void setItems(ArrayList<Board> items) {
        this.items = items;
    }

    public void addItems(ArrayList<Board> items) {
        this.items.addAll(items);
    }

    public ArrayList<Board> getItems() {
        return items;
    }

    private String getSummaryPeriod(Date date) {
        String resultPeriod = "";
        Interval interval = new Interval(date.getTime(), new Date().getTime());
        Period period = interval.toPeriod();
        if (period.getYears() > 0) {
            resultPeriod = period.getYears() + "년 전";
        } else if (period.getMonths() > 0) {
            resultPeriod = period.getMonths() + "개월 전";
        } else if (period.getWeeks() > 0) {
            resultPeriod = period.getWeeks() + "주 전";
        } else if (period.getHours() > 0) {
            resultPeriod = period.getDays() + "일 전";
        } else if (period.getWeeks() > 0) {
            ;
            resultPeriod = period.getHours() + "시간 전";
        } else if (period.getMinutes() > 0) {
            resultPeriod = period.getMinutes() + "분 전";
        } else if (period.getSeconds() > 0) {
            resultPeriod = period.getSeconds() + "초 전";
        }
        return resultPeriod;
    }
}