package com.example.test;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity
{
    EditText usename;
    EditText userid;
    EditText userphone;
    EditText usepwd;
    EditText usepwd2;
    Button submit;
    Database Database;
    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usename = this.findViewById(R.id.usename);                //用户名编辑框
        userid = this.findViewById(R.id.userid);                //学号编辑框
        userphone = this.findViewById(R.id.userphone);                //手机号编辑框
        usepwd = this.findViewById(R.id.usepwd);                //设置初始密码编辑框
        usepwd2 = this.findViewById(R.id.usepwd2);                //二次输入密码编辑框
        submit = this.findViewById(R.id.submit);                //注册按钮
        Database = new Database(this, "Userinfo", null, 1);      //建数据库
        db = Database.getReadableDatabase();
        sp = this.getSharedPreferences("useinfo", this.MODE_PRIVATE);


        submit.setOnClickListener(new View.OnClickListener()
        {
            boolean flag = true;            //判断用户是否已存在的标志位

            @Override
            public void onClick(View v)
            {
                String name = usename.getText().toString();             //用户名
                String ID = userid.getText().toString();                //学号
                String phone = userphone.getText().toString();          //手机号
                String pwd01 = usepwd.getText().toString();             //密码
                String pwd02 = usepwd2.getText().toString();            //二次输入的密码
                if (name.equals("") || pwd01.equals("") || pwd02.equals(""))
                {
                    Toast.makeText(Register.this, "用户名或密码不能为空!！", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Cursor cursor = db.query("login", new String[]{"usname"}, null, null, null, null, null);

                    while (cursor.moveToNext())
                    {
                        if (cursor.getString(0).equals(name))
                        {
                            flag = false;
                            break;
                        }
                    }
                    if (flag == true)
                    {                                             //判断用户是否已存在
                        if (pwd01.equals(pwd02))
                        {                                //判断两次输入的密码是否一致，若一致则继续，不一致则提醒密码不一致
                            ContentValues cv = new ContentValues();
                            cv.put("usname", name);
                            cv.put("userid", ID);
                            cv.put("userphone", phone);
                            cv.put("uspwd", pwd01);
                            db.insert("login", null, cv);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("usname", name);
                            editor.putString("userid", ID);
                            editor.putString("userphone", phone);
                            editor.putString("uspwd", pwd01);
                            editor.commit();
                            Intent intent = new Intent();
                            intent.setClass(Register.this, MainActivity.class);      //跳转到登录页面
                            startActivity(intent);
                            Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(Register.this, "密码不一致！", Toast.LENGTH_LONG).show();            //提示密码不一致
                        }
                    }
                    else
                    {
                        Toast.makeText(Register.this, "用户已存在！", Toast.LENGTH_LONG).show();            //提示密码不一致
                    }

                }
            }


        });
    }
}
