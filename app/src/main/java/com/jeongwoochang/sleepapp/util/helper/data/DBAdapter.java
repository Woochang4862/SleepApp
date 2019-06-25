package com.jeongwoochang.sleepapp.util.helper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.jeongwoochang.sleepapp.util.data.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DBAdapter {
    private static AtomicInteger mOpenCounter = new AtomicInteger();

    protected static final int DATABASE_VERSION = 1;
    protected static final String DB_NAME = "sleep_app.db";

    private final static String LOG_TAG = DBAdapter.class.getSimpleName();

    //Sleep Time Table
    public static final String TABLE_SLEEP_TIME = "sleep_time";
    public static final String SLEEP_TIME_ID = "_id";
    public static final String SLEEP_START_TIME = "sleep_start_time";
    public static final String SLEEP_END_TIME = "sleep_end_time";
    public static final String[] SLEEP_TIME_COLUMNS = {SLEEP_TIME_ID, SLEEP_START_TIME, SLEEP_END_TIME};

    //Fast Achievement Table
    public static final String TABLE_FAST_ACHIEVEMENT = "fast_achievement";
    public static final String FAST_ACHIEVEMENT_ID = "fast_date";
    public static final String FAST_ACHIEVEMENT_SUCCESS = "fast_success";
    public static final String[] FAST_ACHIEVEMNT_COLUMNS = {FAST_ACHIEVEMENT_ID, FAST_ACHIEVEMENT_SUCCESS};

    //Nap Time Table
    public static final String TABLE_NAP_TIME = "nap_time";
    public static final String NAP_TIME_ID = "nap_time_date";
    public static final String NAP_TIME = "nap_time";
    public static final String[] NAP_TIME_COLUMNS = {NAP_TIME_ID, NAP_TIME};

    //WorkOut Table
    public static final String TABLE_WORK_OUT = "work_out";
    public static final String WORK_OUT_ID = "work_out_id";
    public static final String WORK_OUT_TITLE = "work_out_title";
    public static final String WORK_OUT_SUN = "work_out_sun";
    public static final String WORK_OUT_MON = "work_out_mon";
    public static final String WORK_OUT_TUE = "work_out_tue";
    public static final String WORK_OUT_WED = "work_out_wed";
    public static final String WORK_OUT_THU = "work_out_thu";
    public static final String WORK_OUT_FRI = "work_out_fri";
    public static final String WORK_OUT_SAT = "work_out_sat";
    public static final String WORK_OUT_START_TIME = "work_out_start_time";
    public static final String WORK_OUT_END_TIME = "work_out_end_time";
    public static final String[] WORK_OUT_COLUMNS = {WORK_OUT_ID, WORK_OUT_TITLE, WORK_OUT_SUN, WORK_OUT_MON, WORK_OUT_TUE, WORK_OUT_WED, WORK_OUT_THU, WORK_OUT_FRI, WORK_OUT_SAT, WORK_OUT_START_TIME, WORK_OUT_END_TIME};

    //TodayWorkOut Table
    public static final String TABLE_TODAY_WORK_OUT = "today_work_out";
    public static final String TODAY_WORK_OUT_ID = "today_work_out_id";
    public static final String TODAY_WORK_OUT_TITLE = "today_work_out_title";
    public static final String TODAY_WORK_OUT_START_TIME = "today_work_out_start_time";
    public static final String TODAY_WORK_OUT_END_TIME = "today_work_out_end_time";
    public static final String TODAY_WORK_OUT_SUCCESS = "today_work_out_success";
    public static final String[] TODAY_WORK_OUT_COLUMNS = {TODAY_WORK_OUT_ID, TODAY_WORK_OUT_TITLE, TODAY_WORK_OUT_START_TIME, TODAY_WORK_OUT_END_TIME, TODAY_WORK_OUT_SUCCESS};

    //TO.DO Table
    public static final String TABLE_TODO = "todo";
    public static final String TODO_ID = "todo_id";
    public static final String TODO_DATE = "todo_date";
    public static final String TODO_NAME = "todo_name";
    public static final String TODO_SUCCESS = "todo_success";
    public static final String[] TODO_COLUMNS = {TODO_ID, TODO_DATE, TODO_NAME, TODO_SUCCESS};

    private Context context;
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase connection;

    private static DBAdapter instance;

    public static void connect(Context context) {
        if(mOpenCounter.incrementAndGet() == 1) {
            instance.context = context;
            instance.dbHelper = new DBHelper(instance.context);
            instance.connection = instance.dbHelper.getWritableDatabase();
            Timber.plant(new Timber.DebugTree());
            Timber.tag(LOG_TAG);
        }
    }

    private DBAdapter() {
    }

    public synchronized static DBAdapter getInstance() {
        if (instance == null) {
            instance = new DBAdapter();
        }
        return instance;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context) {
            super(context, DB_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_SLEEP_TIME + "(" + SLEEP_TIME_ID + " INTEGER PRIMARY KEY, " + SLEEP_START_TIME + " TEXT, " + SLEEP_END_TIME + " TEXT)");
            db.execSQL("CREATE TABLE " + TABLE_FAST_ACHIEVEMENT + "(" + FAST_ACHIEVEMENT_ID + " INTEGER PRIMARY KEY, " + FAST_ACHIEVEMENT_SUCCESS + " INTEGER DEFAULT 0)");
            db.execSQL("CREATE TABLE " + TABLE_NAP_TIME + "(" + NAP_TIME_ID + " INTEGER PRIMARY KEY, " + NAP_TIME + " INTEGER)");
            db.execSQL("CREATE TABLE " + TABLE_WORK_OUT + "(" +
                    WORK_OUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WORK_OUT_TITLE + " TEXT, " +
                    WORK_OUT_SUN + " INTEGER, " +
                    WORK_OUT_MON + " INTEGER, " +
                    WORK_OUT_TUE + " INTEGER, " +
                    WORK_OUT_WED + " INTEGER, " +
                    WORK_OUT_THU + " INTEGER, " +
                    WORK_OUT_FRI + " INTEGER, " +
                    WORK_OUT_SAT + " INTEGER, " +
                    WORK_OUT_START_TIME + " TEXT, " +
                    WORK_OUT_END_TIME + " TEXT" +
                    ")");
            db.execSQL("CREATE TABLE " + TABLE_TODAY_WORK_OUT + "(" +
                    TODAY_WORK_OUT_ID + " INTEGER PRIMARY KEY, " +
                    TODAY_WORK_OUT_TITLE + " TEXT, " +
                    TODAY_WORK_OUT_START_TIME + " TEXT, " +
                    TODAY_WORK_OUT_END_TIME + " TEXT," +
                    TODAY_WORK_OUT_SUCCESS + " INTEGER" +
                    ")");
            db.execSQL("CREATE TABLE " + TABLE_TODO + "(" +
                    TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TODO_DATE + " INTEGER, " +
                    TODO_NAME + " TEXT, " +
                    TODO_SUCCESS + " INTEGER" +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP_TIME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAST_ACHIEVEMENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAP_TIME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK_OUT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAY_WORK_OUT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    /**
     * DB접속을 종료합니다.
     * 하지만 이 메소드를 호출하게 되면 dbHelper.getWritableDatabase() 값이 null이 되어버리므로 주의
     */
    public void close() {
        try {
            if (mOpenCounter.decrementAndGet() == 0) {
                connection.close();
                dbHelper.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 수면시간 초기 설정합니다
     *
     * @param sleepTimes
     */
    public void initSleepTime(List<SleepTime> sleepTimes) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < sleepTimes.size(); i++) {
            values.put(SLEEP_TIME_ID, i);
            values.put(SLEEP_START_TIME, sleepTimes.get(i).getStart());
            values.put(SLEEP_END_TIME, sleepTimes.get(i).getEnd());
            connection.insertOrThrow(TABLE_SLEEP_TIME, null, values);
        }
    }

    /**
     * 수면시간 변경합니다
     *
     * @param sleepTimes
     */
    public void updateSleepTimes(List<SleepTime> sleepTimes) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < sleepTimes.size(); i++) {
            values.put(SLEEP_START_TIME, sleepTimes.get(i).getStart());
            values.put(SLEEP_END_TIME, sleepTimes.get(i).getEnd());
            String whereClause = SLEEP_TIME_ID + "=?";
            String whereArgs[] = new String[]{String.valueOf(i)};
            if (connection.update(TABLE_SLEEP_TIME, values, whereClause, whereArgs) == 0) {
                Timber.d("There is no such id row record in DB");
            }
        }
        String logMsg = "sleepTime is saved as [\n";
        for (int i = 0; i < sleepTimes.size(); i++) {
            logMsg += i + " : " + sleepTimes.get(i).getStart() + " ~ " + sleepTimes.get(i).getEnd() + ",\n";
        }
        logMsg += "]";
        Timber.d(logMsg);
    }

    /**
     * 지정 요일의 수면시을 반환
     *
     * @param day
     * @return
     */
    public SleepTime getSleepTime(int day) {
        Cursor c = connection.query(TABLE_SLEEP_TIME, SLEEP_TIME_COLUMNS, SLEEP_TIME_ID + "=?", new String[]{String.valueOf(day)}, null, null, null, null);
        c.moveToFirst();
        return new SleepTime(c.getString(1), c.getString(2));
    }

    /**
     * 모든 수면시간을 반환
     *
     * @return
     */
    public List<SleepTime> getSleepTimes() {
        List<SleepTime> sleepTimes = new ArrayList<>();
        Cursor c = connection.query(TABLE_SLEEP_TIME, SLEEP_TIME_COLUMNS, null, null, null, null, null, null);
        if (c.getCount() == 0) return sleepTimes;
        c.moveToFirst();
        do {
            sleepTimes.add(new SleepTime(c.getString(1), c.getString(2)));
        } while (c.moveToNext());
        c.close();
        return sleepTimes;
    }

    /**
     * 금식 성공여부 설정 설정합니다
     *
     * @param id, success
     */
    public void setFastAchievement(long id, boolean success) {
        Timber.d(id + " " + success);
        ContentValues values = new ContentValues();
        values.put(FAST_ACHIEVEMENT_ID, id);
        values.put(FAST_ACHIEVEMENT_SUCCESS, success);
        connection.insertOrThrow(TABLE_FAST_ACHIEVEMENT, null, values);
    }

    /**
     * 금식 성공여부 업데이트합니다
     *
     * @param id, success
     */
    public void updateFastAchievement(long id, boolean success) {
        ContentValues values = new ContentValues();
        values.put(FAST_ACHIEVEMENT_ID, id);
        values.put(FAST_ACHIEVEMENT_SUCCESS, success);
        String whereClause = FAST_ACHIEVEMENT_ID + "=?";
        String whereArgs[] = new String[]{String.valueOf(id)};
        if (connection.update(TABLE_FAST_ACHIEVEMENT, values, whereClause, whereArgs) == 0) {
            Timber.d("There is no such id row record in DB");
        }
    }

    /**
     * 지정 날짜의 금식 여부를 반환
     *
     * @param day
     * @return
     */
    public FastAchievement getFastAchievement(long day) {
        Cursor c = connection.query(TABLE_FAST_ACHIEVEMENT, FAST_ACHIEVEMNT_COLUMNS, FAST_ACHIEVEMENT_ID + "=?", new String[]{String.valueOf(day)}, null, null, null, null);
        c.moveToFirst();
        return new FastAchievement(c.getLong(0), c.getInt(1) == 1);
    }

    /**
     * 모든 성공여부를 반환
     *
     * @return
     */
    public ArrayList<FastAchievement> getFastAchievements() {
        ArrayList<FastAchievement> sleepTimes = new ArrayList<>();
        Cursor c = connection.query(TABLE_FAST_ACHIEVEMENT, FAST_ACHIEVEMNT_COLUMNS, null, null, null, null, null, null);
        if (c.getCount() == 0) return sleepTimes;
        c.moveToFirst();
        do {
            sleepTimes.add(new FastAchievement(c.getInt(0), c.getInt(1) == 1));
        } while (c.moveToNext());
        c.close();
        return sleepTimes;
    }

    /**
     * 낮잠 시간 기록
     *
     * @param napTime
     */
    public void saveNapTime(String napTime) {
        if (getNumberOfNapTime(getCurrDate().getMillis()) == 0) {
            setDefaultNapTime();
        }
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss").withZone(DateTimeZone.UTC);
        DateTime d = dtf.parseDateTime(napTime);
        d.withZone(DateTimeZone.UTC);
        ContentValues values = new ContentValues();
        values.put(NAP_TIME_ID, getCurrDate().getMillis());
        values.put(NAP_TIME, d.getMillis() + getNapTime(getCurrDate().getMillis()).getNapTime());
        String whereClause = NAP_TIME_ID + "=?";
        String whereArgs[] = new String[]{String.valueOf(getCurrDate().getMillis())};
        connection.update(TABLE_NAP_TIME, values, whereClause, whereArgs);
        Timber.d("Nap time " + (d.getMillis() + getNapTime(getCurrDate().getMillis()).getNapTime()) + " at " + getCurrDate().getMillis());
    }

    private void setDefaultNapTime() {
        ContentValues values = new ContentValues();
        values.put(NAP_TIME_ID, getCurrDate().getMillis());
        values.put(NAP_TIME, 0);
        connection.insert(TABLE_NAP_TIME, null, values);
    }

    /**
     * [Util]현재 날짜의 DateTime 반환
     *
     * @return DareTIme
     */
    public static DateTime getCurrDate() {
        Timber.d(new DateTime().withYear(new DateTime().getYear()).withMonthOfYear(new DateTime().getMonthOfYear()).withDayOfMonth(new DateTime().getDayOfMonth()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toString());
        return new DateTime().withYear(new DateTime().getYear()).withMonthOfYear(new DateTime().getMonthOfYear()).withDayOfMonth(new DateTime().getDayOfMonth()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
    }

    /**
     * 낮잠 시간 반환
     *
     * @param date
     * @return napTime
     */
    public NapTime getNapTime(long date) {
        Cursor c = connection.query(NAP_TIME, NAP_TIME_COLUMNS, NAP_TIME_ID + "=?", new String[]{String.valueOf(date)}, null, null, null, null);
        c.moveToFirst();
        Timber.d(new NapTime(c.getLong(0), c.getLong(1)).toString());
        return new NapTime(c.getLong(0), c.getLong(1));
    }

    /**
     * 모든 낮잠 시간 반환
     *
     * @return napTimes
     */
    public ArrayList<NapTime> getNapTimes() {
        ArrayList<NapTime> napTimes = new ArrayList<>();
        Cursor c = connection.query(NAP_TIME, NAP_TIME_COLUMNS, null, null, null, null, null, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                napTimes.add(new NapTime(c.getLong(0), c.getLong(1)));
            } while (c.moveToNext());
        }
        return napTimes;
    }

    /**
     * 저장된 낮잠 시간 반환
     *
     * @return numberOfNapTime
     */
    public int getNumberOfNapTime(long date) {
        return connection.query(NAP_TIME, NAP_TIME_COLUMNS, NAP_TIME_ID + "=?", new String[]{String.valueOf(date)}, null, null, null, null).getCount();
    }

    /**
     * 운동 스케줄 추가
     *
     * @param newWorkOut
     */
    public void addWorkOut(WorkOut newWorkOut) {
        if (newWorkOut.getDayOfWeek() != null) {
            ContentValues values = new ContentValues();
            values.put(WORK_OUT_TITLE, newWorkOut.getTitle());
            values.put(WORK_OUT_START_TIME, newWorkOut.getStartTime());
            values.put(WORK_OUT_END_TIME, newWorkOut.getEndTime());
            for (int i = 0; i < newWorkOut.getDayOfWeek().size(); i++) {
                values.put(WORK_OUT_COLUMNS[i + 2], newWorkOut.getDayOfWeek().get(i));
            }
            Timber.d("dayOfWeek is " + newWorkOut.getDayOfWeek().toString());
            connection.insert(TABLE_WORK_OUT, null, values);
        }
    }

    /**
     * 운동 스케줄 제거
     *
     * @param id
     */
    public void removeWorkOut(int id) {
        connection.delete(TABLE_WORK_OUT, WORK_OUT_ID + "=?", new String[]{String.valueOf(id)});
    }

    /**
     * 해당 요일 운동 스케줄 반환
     *
     * @param dayOfWeek
     * @return workOutList
     */
    public ArrayList<WorkOut> getWorkOut(int dayOfWeek) {
        ArrayList<WorkOut> workOutList = new ArrayList<>();
        Timber.d(String.valueOf(dayOfWeek));
        Cursor c = connection.query(TABLE_WORK_OUT, WORK_OUT_COLUMNS, WORK_OUT_COLUMNS[dayOfWeek%7 + 2] + "=?", new String[]{String.valueOf(1)}, null, null, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                ArrayList<Boolean> dayOfWeekList = new ArrayList<>();
                for (int i = 2; i <= 8; i++) {
                    dayOfWeekList.add(c.getInt(i) == 1);
                }
                workOutList.add(new WorkOut(c.getInt(0), c.getString(1), dayOfWeekList, c.getString(9), c.getString(10)));
            } while (c.moveToNext());
        }
        return workOutList;
    }

    /**
     * 해당 요일 운동 스케줄 반환
     *
     * @return workOutList
     */
    public ArrayList<WorkOut> getWorkOut() {
        ArrayList<WorkOut> workOutList = new ArrayList<>();
        Cursor c = connection.query(TABLE_WORK_OUT, WORK_OUT_COLUMNS, null, null, null, null, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                ArrayList<Boolean> dayOfWeekList = new ArrayList<>();
                for (int i = 2; i <= 8; i++) {
                    dayOfWeekList.add(c.getInt(i) == 1);
                }
                workOutList.add(new WorkOut(c.getInt(0), c.getString(1), dayOfWeekList, c.getString(9), c.getString(10)));
            } while (c.moveToNext());
        }
        return workOutList;
    }

    /**
     * 오늘의 운동 스케줄 초기화
     */
    public void resetTodayWorkOut() {
        connection.delete(TABLE_TODAY_WORK_OUT, null, null);
    }

    /**
     * 오늘의 운동 스케줄 설정
     */
    public void setTodayWorkOut() {
        resetTodayWorkOut();
        DateTime cd = new DateTime();
        ArrayList<WorkOut> tmp = getWorkOut(cd.getDayOfWeek());
        Timber.d("setTodayWorkOut's tmp = " + tmp);
        for (WorkOut workOut : tmp) {
            ContentValues values = new ContentValues();
            values.put(TODAY_WORK_OUT_ID, workOut.get_id());
            values.put(TODAY_WORK_OUT_TITLE, workOut.getTitle());
            values.put(TODAY_WORK_OUT_START_TIME, workOut.getStartTime());
            values.put(TODAY_WORK_OUT_END_TIME, workOut.getEndTime());
            values.put(TODAY_WORK_OUT_SUCCESS, false);
            connection.insert(TABLE_TODAY_WORK_OUT, null, values);
        }
    }

    /**
     * 오늘의 운동 스케줄 반환
     *
     * @return todayWorkOutList
     */
    public ArrayList<TodayWorkOut> getTodayWorkOut() {
        ArrayList<TodayWorkOut> todayWorkOutList = new ArrayList<>();
        Cursor c = connection.query(TABLE_TODAY_WORK_OUT, TODAY_WORK_OUT_COLUMNS, null, null, null, null, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                todayWorkOutList.add(new TodayWorkOut(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4) == 1));
            } while (c.moveToNext());
        }
        return todayWorkOutList;
    }

    /**
     * 오늘의 운동 성공여부 설정
     *
     * @param id, success
     */
    public void setTodayWorkOutSuccess(int id, boolean success) {
        TodayWorkOut tmpTWO = null;
        for (TodayWorkOut todayWorkOut : getTodayWorkOut()) {
            if (todayWorkOut.get_id() == id) {
                tmpTWO = todayWorkOut;
                break;
            }
        }
        if (tmpTWO != null) {
            ContentValues values = new ContentValues();
            values.put(TODAY_WORK_OUT_ID, id);
            values.put(TODAY_WORK_OUT_TITLE, tmpTWO.getTitle());
            values.put(TODAY_WORK_OUT_START_TIME, tmpTWO.getStartTime());
            values.put(TODAY_WORK_OUT_END_TIME, tmpTWO.getEndTime());
            values.put(TODAY_WORK_OUT_SUCCESS, success);
            connection.update(TABLE_TODAY_WORK_OUT, values, TODAY_WORK_OUT_ID + "=?", new String[]{String.valueOf(id)});
        }
    }

    /**
     * 할일 추가
     *
     * @param todo
     */
    public void addTODO(TODO todo) {
        ContentValues values = new ContentValues();
        values.put(TODO_DATE, todo.getDate());
        values.put(TODO_NAME, todo.getName());
        values.put(TODO_SUCCESS, false);
        connection.insert(TABLE_TODO, null, values);
    }

    /**
     * 해당 날짜의 할일 반환
     */
    public ArrayList<TODO> getTODO(long date) {
        ArrayList<TODO> todoList = new ArrayList<>();

        Cursor c = connection.query(TABLE_TODO, TODO_COLUMNS, TODO_DATE + "=?", new String[]{String.valueOf(date)}, null, null, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                todoList.add(new TODO(c.getInt(0), c.getLong(1), c.getString(2), c.getInt(3) == 1));
            } while (c.moveToNext());
        }
        Timber.d(date + " todoList is " + todoList);
        return todoList;
    }

    public ArrayList<TODO> getTODO(DateTime date) {
        date = date.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        return getTODO(date.getMillis());
    }

    public void setTODOSuccess(int id, boolean isSuccess){
        Cursor c = connection.query(TABLE_TODO, TODO_COLUMNS, TODO_ID+"=?",new String[]{String.valueOf(id)}, null, null, null);
        c.moveToFirst();
        ContentValues values = new ContentValues();
        values.put(TODO_ID, c.getInt(0));
        values.put(TODO_DATE, c.getLong(1));
        values.put(TODO_NAME, c.getString(2));
        values.put(TODO_SUCCESS, isSuccess);
        connection.update(TABLE_TODO, values, TODO_ID+"=?",new String[]{String.valueOf(id)});
    }

    public void removeTODO(int id) {
        connection.delete(TABLE_TODO, TODO_ID + "=?", new String[]{String.valueOf(id)});
    }
}
