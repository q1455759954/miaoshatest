package com.example.miaoshatest.common;

public class Test {

    public static void main(String[] args){

        String str = "<nf:textInput labelValue=\"业务流水号\" name=\"piid\" dataType=\"string\" hidden=\"true\"></nf:textInput>\n" +
                "            <nf:textInput labelValue=\"业务id\" name=\"ywid\" dataType=\"string\" hidden=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"申报编号\" name=\"grsbbh\" dataType=\"string\" hidden=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"个人编号\" name=\"grbh\" dataType=\"string\" hidden=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:dropdown labelValue=\"数据来源\" name=\"sjly\" arrayCode=\"0:个人申报,1:线下导入审核通过数据\"  hidden=\"true\"></nf:dropdown>\n" +
                "\t\t\t<nf:dropdown name=\"shjb\" labelValue=\"审核级别\" code=\"SHJB\"  hidden=\"true\"></nf:dropdown>\n" +
                "\t\t\t<nf:textInput labelValue=\"姓名\" name=\"xm\" dataType=\"string\" widthFixed=\"true\"  readonly=\"true\" maxLength=\"20\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"外文名\" name=\"wwm\" dataType=\"string\" widthFixed=\"true\" required=\"true\" maxLength=\"20\"></nf:textInput>\n" +
                "\t\t\t<nf:dropdown name=\"zgrych\" code=\"RYCH\" labelValue=\"最高荣誉称号\" required=\"true\" onchange=\"setBffs()\"></nf:dropdown>\n" +
                "\t\t\t<nf:dropdown labelValue=\"颁发方式\" name=\"bffs\" code=\"BFFS\" widthFixed=\"true\" readonly=\"true\"></nf:dropdown>\n" +
                "  \t\t\t<nf:dropdown name=\"zjlx\" code=\"YXZJLX\" labelValue=\"证件类型\"  readonly=\"true\" ></nf:dropdown>\n" +
                "\t\t\t<nf:textInput labelValue=\"证件号码\" name=\"zjhm\" dataType=\"string\"  readonly=\"true\" maxLength=\"20\" ></nf:textInput>\n" +
                "\t\t\t<nf:dropdown labelValue=\"性别\" name=\"xb\" code=\"XB\" readonly=\"true\"  ></nf:dropdown>\n" +
                "\t\t\t<nf:textInput labelValue=\"出生日期\" name=\"csrq\" dataType=\"date\" mask=\"yyyy-MM-dd\"  readonly=\"true\" maxLength=\"20\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"出生地\" name=\"csd\" dataType=\"string\" required=\"true\" ></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"联系电话\" name=\"lxdh\" dataType=\"string\"  required=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"通讯地址\" name=\"txdz\" dataType=\"string\" required=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"邮箱\" name=\"dzyx\" dataType=\"string\"  required=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:dropdown name=\"mz\" code=\"MZ\" labelValue=\"民族\" required=\"true\"></nf:dropdown>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"gj\" labelValue=\"籍贯\" required=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:dropdown name=\"zzmm\" code=\"ZZMM\" labelValue=\"政治面貌\" required=\"true\"></nf:dropdown>\n" +
                "\t\t\t<nf:dropdown name=\"sflx\" code=\"SF\" labelValue=\"是否留学\" required=\"true\" onchange=\"getXlInfo()\"></nf:dropdown>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"lxgb\" labelValue=\"留学国别\" ></nf:textInput>\n" +
                "\t\t\t<nf:dropdown name=\"sfwyjrc\" code=\"SF\" labelValue=\"是否为引进人才\" required=\"true\"></nf:dropdown>\n" +
                "\t\t\t<nf:dropdown  code=\"YJFS\" name=\"yjfs\" labelValue=\"引进方式\" ></nf:dropdown>\n" +
                "\t\t\t<nf:textInput labelValue=\"毕业院校\" name=\"byyx\" dataType=\"string\"  required=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:dropdown name=\"zymc\" code=\"ZYLB\" labelValue=\"专业名称\" required=\"true\"></nf:dropdown>\n" +
                "\t\t\t<nf:textInput labelValue=\"专业领域\" name=\"zyly\" dataType=\"string\" required=\"true\" ></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"研究方向\" name=\"yjfx\" dataType=\"string\" required=\"true\" ></nf:textInput>\n" +
                "\t\t\t<nf:dropdown name=\"xl\" code=\"XXPTXL\" labelValue=\"学历\" required=\"true\"></nf:dropdown>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"xlzsbh\" labelValue=\"学历证书编号\" required=\"true\" onchange=\"getXlInfo()\"></nf:textInput>\n" +
                "\t\t\t<nf:dropdown name=\"xw\" code=\"XW\" labelValue=\"学位\" required=\"true\"></nf:dropdown>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"xwzsbh\" labelValue=\"学位证书编号\" required=\"true\" onchange=\"getXwInfo()\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput labelValue=\"学习形式\" name=\"xxxs\" dataType=\"string\"  required=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput dataType=\"Date\" name=\"rxrq\" labelValue=\"入学日期\" mask=\"yyyy-MM-dd\" required=\"true\"></nf:textInput>\t\t\t\t\t\n" +
                "\t\t\t<nf:textInput dataType=\"Date\" name=\"bysj\" labelValue=\"毕业时间\" mask=\"yyyy-MM-dd\" required=\"true\"></nf:textInput>\t\t\t\t\t\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"dwid\" labelValue=\"单位ID\" required=\"true\" hidden=\"true\"></nf:textInput>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"gzdw\" labelValue=\"工作单位\" readonly=\"true\" ></nf:textInput>\n" +
                "\t\t\t<nf:dropdown name=\"dwssqy\" dsCode=\"jgcode\" labelValue=\"单位所属区域\" readonly=\"true\" ></nf:dropdown>\n" +
                "<%-- \t\t\t<nf:textInput dataType=\"String\" name=\"dwzgbm\" labelValue=\"单位主管部门\" ></nf:textInput> --%>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"zgbmid\" labelValue=\"主管部门id\" hidden=\"true\" required=\"true\"></nf:textInput>\t\n" +
                "\t\t\t<nf:lovInput name=\"dwzgbm\" labelValue=\"单位主管部门\" required=\"true\" lovJS=\"openDwLov()\" fillMapping=\"dwzgbm:zgbmmc,zgbmid:jgbh\" colspan=\"2\" ></nf:lovInput>\n" +
                "\t\t\t\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"zc\" labelValue=\"职称\" ></nf:textInput>\n" +
                "\t\t\t<nf:textInput dataType=\"Date\" name=\"zcqdsj\" labelValue=\"职称取得时间\" mask=\"yyyy-MM-dd\" ></nf:textInput>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"zyzg\" labelValue=\"职业资格\" ></nf:textInput>\n" +
                "\t\t\t<nf:textInput dataType=\"Date\" name=\"zyzgqdsj\" labelValue=\"职业资格取得时间\" mask=\"yyyy-MM-dd\" ></nf:textInput>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"zw\" labelValue=\"职务\"  ></nf:textInput>\n" +
                "<%-- \t\t\t<nf:textInput dataType=\"String\" name=\"xxgzjl\" labelValue=\"学习工作经历\" ></nf:textInput> --%>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"yjjj\" labelValue=\"业绩简介\" ></nf:textInput>\n" +
                "\t\t\t<nf:textInput dataType=\"String\" name=\"htzt\" labelValue=\"合同状态\" ></nf:textInput>\n" +
                "\t\t\t<nf:textInput dataType=\"Date\" name=\"cjgzsj\" labelValue=\"参加工作时间\" mask=\"yyyy-MM-dd\" ></nf:textInput>";
        String[] a = str.split(">");
        StringBuffer sb = new StringBuffer();
        for (String q : a){
            boolean flag = false;
            if (q.contains("readonly") || q.contains("required")){
                for (String p : q.split(" ")){
                    if (p.contains("labelValue")){
                        sb.append(p.split("\"")[1]);
                        sb.append(":");
                    }
                    if (p.contains("dropdown")){
                        flag=true;
                    }
                }
                if (flag){
                    for (String p : q.split(" ")){
                        if (p.contains("name")){
                            sb.append(p.split("\"")[1]);
                            sb.append(":");
                        }
                    }
                    for (String p : q.split(" ")){
                        if (p.contains("code")){
                            sb.append(p.split("\"")[1]);
                            sb.append(",");
                        }
                    }
                }else {
                    for (String p : q.split(" ")){
                        if (p.contains("name")){
                            sb.append(p.split("\"")[1]);
                            sb.append(",");
                        }
                    }
                }

            }
        }
        System.out.println(sb.toString());

    }

}
