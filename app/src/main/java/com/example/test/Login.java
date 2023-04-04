package com.example.test;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.database.UserDBHelper;
import com.example.test.enity.Daka;
import com.example.test.util.DateUtil;
import com.example.test.util.ToastUtil;

import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener
{
    private EditText et_keyword;
    private EditText et_summary;
    private UserDBHelper mHelper;
    private TextView showInfo;

    private TextView tv_result;
    private TextView tv_date;

    private SQLiteDatabase db;
    private Cursor cursor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_keyword = findViewById(R.id.et_keyword);
        et_summary = findViewById(R.id.et_summary);
        showInfo = findViewById(R.id.showInfo);
        tv_result = findViewById(R.id.tv_result);
        tv_date = findViewById(R.id.tv_date);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_time).setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
        findViewById(R.id.btn_query_keyword).setOnClickListener(this);
        findViewById(R.id.btn_query_summary).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
        findViewById(R.id.btn_new).setOnClickListener(this);

    }


    protected void onStart()
    {
        super.onStart();
        // 获得数据库帮助器的实例
        mHelper = UserDBHelper.getInstance(this);
        // 打开数据库帮助器的读写连接
        mHelper.openWriteLink();
        mHelper.openReadLink();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        // 关闭数据库连接
        mHelper.closeLink();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v)
    {
        db = mHelper.getReadableDatabase();
        //String date = et_date.getText().toString();
        String date = String.format("%s", DateUtil.getNowTime());
        String keyword = et_keyword.getText().toString();
        String summary = et_summary.getText().toString();
        switch (v.getId())
        {
            case R.id.btn_save://添加
                cursor = db.query("daka_info", new String[]{"date"}, null, null, null, null, null, null);
                cursor.moveToLast();
                String lasttime = cursor.getString(0);
                cursor.close();
                if (date.equals("") || keyword.equals("") || summary.equals(""))
                {
                    ToastUtil.show(this, "打卡失败，不允许输入有空");
                    break;
                }
                else if (date.equals(lasttime))
                {
                    ToastUtil.show(this, "打卡失败，今日已打卡");
                    break;
                }
                else
                {
                    db.execSQL("insert into daka_info(date,keyword,summary)values(?,?,?)", new Object[]{date, keyword, summary});
                    ToastUtil.show(this, "打卡成功");
                    break;
                }

            case R.id.btn_time://闹钟
                Intent intent = new Intent(Login.this, TimeActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_delete://删除
                int i = db.delete("daka_info", "keyword=?", new String[]{keyword});
                //db.execSQL("delete from daka_info where keyword=?",new Object[]{keyword});
                if (i > 0)
                {
                    ToastUtil.show(this, "成功删除" + i + "条打卡内容");
                }
                else
                {
                    ToastUtil.show(this, "删除失败");
                }
                break;

            case R.id.btn_update://修改
                ContentValues cv = new ContentValues();
                cv.put("keyword", keyword);
                cv.put("summary", summary);
                db.update("daka_info", cv, "date=?", new String[]{date});

                //db.execSQL("update daka_info set keyword=?,summary=? where date=?",new Object[]{keyword,summary,date});
                ToastUtil.show(this, "修改成功");
                break;

            case R.id.btn_query://查询所有
                showInfo.setText("");
                //Cursor：结果集，结果集中有游标，游标会指向结果集中的某条记录，游标指向哪条记录，我们就会获取哪一条，初始时指向第一条记录
                cursor = db.query("daka_info", new String[]{"date", "keyword", "summary"}, null, null, null, null, null, null);
                while (cursor.moveToNext())
                {
                    showInfo.append("\n" + "日期:" + cursor.getString(0) + "    关键字:" + cursor.getString(1) + "   总结:" + cursor.getString(2));
                }
                cursor.close();
                break;

            case R.id.btn_query_keyword://按关键词查询
                showInfo.setText("");
                //Cursor：结果集，结果集中有游标，游标会指向结果集中的某条记录，游标指向哪条记录，我们就会获取哪一条，初始时指向第一条记录
                cursor = db.query("daka_info", new String[]{"date", "keyword", "summary"}, "keyword = ?", new String[]{keyword}, null, null, null, null);
                while (cursor.moveToNext())
                {
                    showInfo.append("\n" + "日期:" + cursor.getString(0) + "    关键字:" + cursor.getString(1) + "   总结:" + cursor.getString(2));
                }
                cursor.close();
                break;

            case R.id.btn_query_summary://按每日总结查询
                showInfo.setText("");
                //Cursor：结果集，结果集中有游标，游标会指向结果集中的某条记录，游标指向哪条记录，我们就会获取哪一条，初始时指向第一条记录
                cursor = db.query("daka_info", new String[]{"date", "keyword", "summary"}, "summary=?", new String[]{summary}, null, null, null, null);
                while (cursor.moveToNext())
                {
                    showInfo.append("\n" + "日期:" + cursor.getString(0) + "    关键字:" + cursor.getString(1) + "   总结:" + cursor.getString(2));
                }
                cursor.close();
                break;

            case R.id.btn_new:
                showInfo.setText("");
                cursor = db.query("daka_info", new String[]{"date"}, null, null, null, null, null, null);
                String j = String.valueOf(cursor.getCount());//获取数据行数
                cursor.moveToLast();//移到最后一行
                lasttime = cursor.getString(0);//获取第一列的内容
                tv_result.setText("打卡天数:" + j + "，上次打卡:" + lasttime);
                tv_date.setText("今天日期" + date);
                cursor.close();
                break;
        }
    }
}