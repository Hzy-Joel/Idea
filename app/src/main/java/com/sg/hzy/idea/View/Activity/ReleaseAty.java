package com.sg.hzy.idea.View.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sg.hzy.idea.DataClass.Labels;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.UI.MyCheckBox;
import com.sg.hzy.idea.UI.MyRadioButton;
import com.sg.hzy.idea.UI.richedittext.bean.FontStyle;
import com.sg.hzy.idea.UI.richedittext.handle.CustomHtml;
import com.sg.hzy.idea.UI.richedittext.handle.Utils;
import com.sg.hzy.idea.UI.richedittext.view.RichEditText;
import com.sg.hzy.idea.Utils.GlobalField;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by 胡泽宇 on 2018/12/5.
 */

public class ReleaseAty extends Activity {


    private boolean isBold;//是否选中了加粗
    private boolean isInter;//是否选中了斜体
    private TextView tv_labels;
    String[] labels;
    private String style = "";
    private String content = "";
    private StringBuilder textcontext = new StringBuilder();
    //                    加粗按钮      字体大小按钮  字体对齐方式    字体颜色
    private MyRadioButton rbFontBold, rbFontSize, rbFontAlign, rbFontColor;
    // 字体加粗选项，多选      加粗         斜体          下划线
    private MyCheckBox cbFontBold, cbFontInter, cbFontLine;
    //                    字体大小区           加粗区           对齐方式区        颜色区
    private LinearLayout llFontSizeArea, llFontBoldArea, llFontAlignArea, llFontColorArea;
    //                    加粗区      字体大小区    颜色区        对齐方式区
    private RadioGroup rgFontBold, rgFontSize, rgFontColor, rgFontAlign;
    //底部字体控制栏
    private LinearLayout llFontControl;
    //内容区
    private RichEditText etContent;
    private ImageView iv_insertimg;

    private ImageButton ibpost;

    private Boolean needcreateLabel = false;
    private String lable;
    private String Title;
    private TextView tv_title;
    private Boolean setlabelok=false;
    private Boolean setTitleok=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_release);
        initobject();
    }

    private void initobject() {
        ibpost = findViewById(R.id.aty_release_ibpost);
        tv_labels = findViewById(R.id.aty_release_tv_label);
        tv_title=findViewById(R.id.aty_release_et_title);
        rbFontSize = findViewById(R.id.iv_font_option_a);
        rbFontBold = findViewById(R.id.iv_font_option_b);
        etContent = findViewById(R.id.et_txteditor_content);
        rbFontAlign = findViewById(R.id.iv_font_option_center);
        rbFontColor = findViewById(R.id.iv_font_option_color);
        llFontSizeArea = findViewById(R.id.ll_font_option_a);
        llFontBoldArea = findViewById(R.id.ll_txteditor_style_area);
        llFontAlignArea = findViewById(R.id.ll_font_option_center);
        llFontColorArea = findViewById(R.id.ll_font_option_color);
        rgFontSize = findViewById(R.id.rg_font_option_a);
        rgFontBold = findViewById(R.id.rg_font_option_b);
        rgFontColor = findViewById(R.id.rg_font_option_color);
        rgFontAlign = findViewById(R.id.rg_font_option_center);
        llFontControl = findViewById(R.id.ll_font_option_area);
        cbFontBold = findViewById(R.id.mcb_font_option_border);
        cbFontInter = findViewById(R.id.mcb_font_option_inter);
        cbFontLine = findViewById(R.id.mcb_font_option_line);
        iv_insertimg = findViewById(R.id.tv_txteditor_addlinked);
        etContent.setFontSize(FontStyle.NORMAL);
        rbFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFontSizeArea.setVisibility(View.VISIBLE);
                llFontBoldArea.setVisibility(View.GONE);
                llFontAlignArea.setVisibility(View.GONE);
                llFontColorArea.setVisibility(View.GONE);
            }
        });
        iv_insertimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType("image/*");
                startActivityForResult(getAlbum, 0);
            }
        });
        rbFontBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFontSizeArea.setVisibility(View.GONE);
                llFontBoldArea.setVisibility(View.VISIBLE);
                llFontAlignArea.setVisibility(View.GONE);
                llFontColorArea.setVisibility(View.GONE);
            }
        });
        rbFontAlign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFontSizeArea.setVisibility(View.GONE);
                llFontBoldArea.setVisibility(View.GONE);
                llFontAlignArea.setVisibility(View.VISIBLE);
                llFontColorArea.setVisibility(View.GONE);
            }
        });
        rbFontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFontSizeArea.setVisibility(View.GONE);
                llFontBoldArea.setVisibility(View.GONE);
                llFontAlignArea.setVisibility(View.GONE);
                llFontColorArea.setVisibility(View.VISIBLE);
            }
        });
        llFontControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgFontBold.clearCheck();
                llFontSizeArea.setVisibility(View.GONE);
                llFontBoldArea.setVisibility(View.GONE);
                llFontAlignArea.setVisibility(View.GONE);
                llFontColorArea.setVisibility(View.GONE);
            }
        });
        tv_labels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCreateLabel();
            }
        });
        ibpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibpost.setClickable(false);
                if(Title==null){
                    setTitleok=false;
                }else{
                    if(Title.length()==0||Title==""){
                        setTitleok=false;
                    }
                }
                //发布 弹出标题主题框
                if(!setTitleok) {
                    ShowCreateTitle();
                    ibpost.setClickable(true);
                }else {
                    if (etContent.getText().toString().length() == 0 || !setlabelok) {
                        Toast.makeText(ReleaseAty.this, "标签和内容不能为空！", Toast.LENGTH_SHORT).show();
                        ibpost.setClickable(true);
                    } else {
                        final SweetAlertDialog pDialog = new SweetAlertDialog(ReleaseAty.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("发布中...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        textcontext.insert(0,etContent.getEditableText().toString());
                        if (needcreateLabel) {
                            GPModel.getInstance().CreateNewLabel(lable, new BaseModel.CreateLabelListener() {
                                @Override
                                public void fail(String error) {
                                    ibpost.setClickable(true);
                                    Toast.makeText(ReleaseAty.this, "发布失败!"+error, Toast.LENGTH_SHORT).show();

                                    pDialog.dismiss();
                                }

                                @Override
                                public void process() {

                                }

                                @Override
                                public void success(final Labels labels) {
                                    //组装发布动态
                                    CustomHtml.toHtml(etContent.getEditableText(), CustomHtml.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE, new BaseModel.GetHtml() {
                                                @Override
                                                public void process() {

                                                }

                                                @Override
                                                public void success(StringBuilder html) {
                                                    Log.i("richText", "html:" + html);
                                                    GPModel.getInstance().PostDyanmicMessage(Title, textcontext, html.toString(), labels, new BaseModel.DoneListener() {
                                                        @Override
                                                        public void fail(String error) {
                                                            ibpost.setClickable(true);
                                                            Toast.makeText(ReleaseAty.this, "发布失败!"+error, Toast.LENGTH_SHORT).show();
                                                            pDialog.dismiss();
                                                        }

                                                        @Override
                                                        public void process() {

                                                        }

                                                        @Override
                                                        public void success() {
                                                            GPModel.getInstance().setPostheadpicurl(null);
                                                            Toast.makeText(ReleaseAty.this, "发布成功!", Toast.LENGTH_SHORT).show();
                                                            ReleaseAty.this.finish();
                                                            pDialog.dismiss();
                                                        }
                                                    });

                                                }


                                            }
                                    );

                                }
                            });
                        } else {
                            CustomHtml.toHtml(etContent.getEditableText(), CustomHtml.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE, new BaseModel.GetHtml() {
                                        @Override
                                        public void process() {

                                        }

                                        @Override
                                        public void success(final StringBuilder html) {
                                            Log.i("richText", "html:" + html);
                                            GPModel.getInstance().FindLabelByStr(lable, new BaseModel.GetLabelsLinstener() {
                                                @Override
                                                public void fail(String error) {
                                                    pDialog.dismiss();
                                                    Toast.makeText(ReleaseAty.this, "发布失败!"+error, Toast.LENGTH_SHORT).show();

                                                    ibpost.setClickable(true);
                                                }

                                                @Override
                                                public void success(List<Labels> userList) {
                                                    GPModel.getInstance().PostDyanmicMessage(Title, textcontext, html.toString(), userList.get(0), new BaseModel.DoneListener() {
                                                        @Override
                                                        public void fail(String error) {
                                                            pDialog.dismiss();
                                                            ibpost.setClickable(true);
                                                            Toast.makeText(ReleaseAty.this, "发布失败!"+error, Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void process() {
                                                        }

                                                        @Override
                                                        public void success() {
                                                            pDialog.dismiss();
                                                            GPModel.getInstance().setPostheadpicurl(null);
                                                            Toast.makeText(ReleaseAty.this, "发布成功!", Toast.LENGTH_SHORT).show();
                                                            ReleaseAty.this.finish();
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }
                            );
                        }
                    }
                }

            }
        });
//
        setTextSizeListener();
        setTextAlginListener();
        setTextStyleListener();
        setTextColorListener();
        etContent.setFontSize(FontStyle.NORMAL);

    }

    /**
     * 设置字体颜色选择监听
     */
    private void setTextColorListener() {
        findViewById(R.id.mrb_font_option_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_BLACK);
            }
        });
        findViewById(R.id.mrb_font_option_gray).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_GRAY);
            }
        });
        findViewById(R.id.mrb_font_option_blackgray).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_BLACKGRAY);
            }
        });
        findViewById(R.id.mrb_font_option_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_BLUE);
            }
        });
        findViewById(R.id.mrb_font_option_green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_GREEN);
            }
        });
        findViewById(R.id.mrb_font_option_yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_YELLOW);
            }
        });
        findViewById(R.id.mrb_font_option_violet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_VOILET);
            }
        });
        findViewById(R.id.mrb_font_option_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_WHITE);
            }
        });
        findViewById(R.id.mrb_font_option_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setFontColor(GlobalField.FontColor.COLOR_RED);
            }
        });


    }

    private void ShowCreateTitle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑标题");
        final View view = View.inflate(this, R.layout.aty_release_edittitle, null);

        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                EditText editText = view.findViewById(R.id.aty_release_et_title);
                Title = editText.getText().toString();
                if(Title.length()!=0) {
                    tv_title.setText(editText.getText().toString());
                    setTitleok = true;
                }else{
                    setTitleok = true;
                }
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.alpha = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void ShowCreateLabel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择idea关键词");
        View view = View.inflate(this, R.layout.select_label, null);
        final AutoCompleteTextView autotvlabel = (AutoCompleteTextView) view.findViewById(R.id.select_label_auto);
        GPModel.getInstance().GetLabels(new BaseModel.GetLabelsLinstener() {
            @Override
            public void fail(String error) {

            }

            @Override
            public void success(List<Labels> userList) {
                labels = new String[userList.size()];
                for (int i = 0; i < userList.size(); i++) {
                    labels[i] = userList.get(i).getLabel();
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ReleaseAty.this, android.R.layout.simple_dropdown_item_1line, labels);
                autotvlabel.setAdapter(arrayAdapter);

            }
        });
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                tv_labels.setText(" #" + autotvlabel.getText().toString());
                setlabelok=true;
                lable = autotvlabel.getText().toString();
                int i = 0;
                for (; i < labels.length; i++) {
                    if (autotvlabel.getText().toString().equals(labels[i])) {
                        break;
                    }
                }
                if (i >= labels.length ) {
                    needcreateLabel = true;
                } else {
                    needcreateLabel = false;
                }
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.alpha = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    /**
     * 设置字体对齐监听
     */
    private void setTextAlginListener() {
        findViewById(R.id.mrb_font_option_left).setOnClickListener(new View.OnClickListener() {//居左显示
            @Override
            public void onClick(View v) {
                etContent.setGravity(Gravity.LEFT);

            }
        });
        findViewById(R.id.mrb_font_option_center).setOnClickListener(new View.OnClickListener() {//居中显示
            @Override
            public void onClick(View v) {
                etContent.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        });
        findViewById(R.id.mrb_font_option_right).setOnClickListener(new View.OnClickListener() {//居右显示
            @Override
            public void onClick(View v) {
                etContent.setGravity(Gravity.RIGHT);
            }
        });
    }

    /**
     * 设置字体加粗、斜线、下划线监听
     */
    private void setTextStyleListener() {
        findViewById(R.id.mcb_font_option_border).setOnClickListener(new View.OnClickListener() {//加粗
            @Override
            public void onClick(View v) {
                isBold = !isBold;
                if (((MyCheckBox) v).isChecked()) {
                    if (isInter) {
                        //斜体粗体
                        etContent.setBold(true);
                        etContent.setItalic(true);
                    } else {
                        etContent.setBold(true);
                    }
                } else {
                    if (isInter) {
                        etContent.setItalic(true);
                    } else {
                        etContent.setBold(false);
                        etContent.setItalic(false);
                    }
                }
            }
        });
        findViewById(R.id.mcb_font_option_inter).setOnClickListener(new View.OnClickListener() {//斜体
            @Override
            public void onClick(View v) {
                isInter = !isInter;
                if (((MyCheckBox) v).isChecked()) {
                    if (isBold) {
                        etContent.setBold(true);
                        etContent.setItalic(true);
                    } else {
                        etContent.setItalic(true);
                    }
                } else {
                    if (isBold) {
                        etContent.setBold(true);
                    } else {
                        etContent.setBold(false);
                        etContent.setItalic(false);
                    }
                }
            }
        });
        findViewById(R.id.mcb_font_option_line).setOnClickListener(new View.OnClickListener() {//下划线
            @Override
            public void onClick(View v) {
                if (((MyCheckBox) v).isChecked()) {
                    etContent.setUnderline(true);
                } else {
                    etContent.setUnderline(false);
//
                }
            }
        });
    }

    /**
     * 设置字体大小的监听
     */
    private void setTextSizeListener() {
        findViewById(R.id.mrb_font_option_add).setOnClickListener(new View.OnClickListener() {//字体增大
            @Override
            public void onClick(View v) {
                if (((MyRadioButton) v).isChecked()) {
                    etContent.setFontSize(FontStyle.BIG);
                } else {
                    etContent.setFontSize(FontStyle.NORMAL);
                }
            }
        });
        findViewById(R.id.mrb_font_option_normal).setOnClickListener(new View.OnClickListener() {//正常字体
            @Override
            public void onClick(View v) {
                etContent.setFontSize(FontStyle.NORMAL);
            }
        });
        findViewById(R.id.mrb_font_option_sub).setOnClickListener(new View.OnClickListener() {//小号字体
            @Override
            public void onClick(View v) {
                if (((MyRadioButton) v).isChecked()) {
                    etContent.setFontSize(FontStyle.SMALL);
                } else {
                    etContent.setFontSize(FontStyle.NORMAL);
                }
            }
        });
    }

    /**
     * 回显字体加粗方式
     *
     * @param style
     */
    private void setFontBold(String style) {
        if (style.contains(GlobalField.FontBold.KEY_STYLE_BOLD) && style.contains(GlobalField.FontBold.KEY_STYLE_ITALIC)) {//加粗和斜体
            etContent.setBold(true);
            etContent.setItalic(true);
            etContent.setUnderline(false);
            cbFontBold.setChecked(true);
            cbFontInter.setChecked(true);
            isBold = true;
            isInter = true;
        } else if (style.contains(GlobalField.FontBold.KEY_STYLE_BOLD)) {//只用加粗
            isBold = true;
            etContent.setBold(true);
            etContent.setItalic(false);
            etContent.setUnderline(false);
            cbFontBold.setChecked(true);
        } else if (style.contains(GlobalField.FontBold.KEY_STYLE_ITALIC)) {//只用斜体
            isInter = true;
            cbFontInter.setChecked(true);
            etContent.setItalic(true);
            etContent.setBold(false);
            etContent.setUnderline(false);
        }

        if (style.contains(GlobalField.FontBold.KEY_STYLE_UNDERLINE)) {
            cbFontLine.setChecked(true);
            etContent.setUnderline(true);
        }
    }

    /**
     * 回显字体对齐方式
     *
     * @param style
     */
    private void setFontAlign(String style) {
        if (style.contains(GlobalField.FontAlign.KEY_ALIGN_CENTER)) {
            etContent.setGravity(Gravity.CENTER_HORIZONTAL);
            rgFontAlign.check(R.id.mrb_font_option_center);
        } else if (style.contains(GlobalField.FontAlign.KEY_ALIGN_RIGHT)) {
            etContent.setGravity(Gravity.RIGHT);
            rgFontAlign.check(R.id.mrb_font_option_right);
        } else {
            rgFontAlign.check(R.id.mrb_font_option_left);
            etContent.setGravity(Gravity.LEFT);
        }
    }

    /**
     * 回显字体大小
     *
     * @param style
     */
    private void setFontSize(String style) {
        if (style.contains(GlobalField.FontSize.KEY_SIZE_18)) {
            etContent.setFontSize(GlobalField.FontSize.SIZE_18);
            rgFontSize.check(R.id.mrb_font_option_add);
        } else if (style.contains(GlobalField.FontSize.KEY_SIZE_14)) {
            etContent.setFontSize(GlobalField.FontSize.SIZE_14);
            rgFontSize.check(R.id.mrb_font_option_sub);
        } else {
            etContent.setFontSize(GlobalField.FontSize.SIZE_16);
            rgFontSize.check(R.id.mrb_font_option_normal);
        }
    }

    /**
     * 回显字体颜色
     *
     * @param style
     */
    private void setFontColor(String style) {
        if (style.contains(GlobalField.FontColor.KEY_COLOR_GRAY)) {//灰色
            etContent.setFontColor(GlobalField.FontColor.COLOR_GRAY);
            rgFontColor.check(R.id.mrb_font_option_gray);
        } else if (style.contains(GlobalField.FontColor.KEY_COLOR_BLACKGRAY)) {//深蓝色
            etContent.setFontColor(GlobalField.FontColor.COLOR_BLACKGRAY);
            rgFontColor.check(R.id.mrb_font_option_blackgray);
        } else if (style.contains(GlobalField.FontColor.KEY_COLOR_WHITE)) {//白色
            etContent.setFontColor(GlobalField.FontColor.COLOR_WHITE);
            rgFontColor.check(R.id.mrb_font_option_white);
        } else if (style.contains(GlobalField.FontColor.KEY_COLOR_BLUE)) {//蓝色
            etContent.setFontColor(GlobalField.FontColor.COLOR_BLUE);
            rgFontColor.check(R.id.mrb_font_option_blue);
        } else if (style.contains(GlobalField.FontColor.KEY_COLOR_GREEN)) {//绿色
            etContent.setFontColor(GlobalField.FontColor.COLOR_GREEN);
            rgFontColor.check(R.id.mrb_font_option_green);
        } else if (style.contains(GlobalField.FontColor.KEY_COLOR_YELLOW)) {//黄色
            etContent.setFontColor(GlobalField.FontColor.COLOR_YELLOW);
            rgFontColor.check(R.id.mrb_font_option_yellow);
        } else if (style.contains(GlobalField.FontColor.KEY_COLOR_VOILET)) {//紫色
            etContent.setFontColor(GlobalField.FontColor.COLOR_VOILET);
            rgFontColor.check(R.id.mrb_font_option_violet);
        } else if (style.contains(GlobalField.FontColor.KEY_COLOR_RED)) {//红色
            etContent.setFontColor(GlobalField.FontColor.COLOR_RED);
            rgFontColor.check(R.id.mrb_font_option_red);
        } else {
            etContent.setFontColor(GlobalField.FontColor.COLOR_BLACK);
            rgFontColor.check(R.id.mrb_font_option_black);//黑色
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            //上传到数据库
            if(data!=null) {
                Uri originalUri = data.getData(); // 获得图片的uri
                String path = Utils.getRealPathFromUri(this, originalUri);
                etContent.setImg(path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
