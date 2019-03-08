package com.gzdb.yct.util;

import android.content.Context;
import android.view.View;

import com.core.util.ToastUtil;
import com.gzdb.sale.dialog.NoticeDialog;

public class ReaderError {

    public static void code(Context context, byte code) {
        String msg = "";
        switch (code) {
            case 0x60:
                msg = "没有安装 SAM 卡";
                break;
            case 0x61:
                msg = "SAM 卡初始化错误或未初始化";
                break;
            case 0x62:
                msg = "SAM 卡检验 PIN 错误";
                break;
            case 0x63:
                msg = "SAM 卡类型与交易类型不匹配";
                break;
            case 0x64:
                msg = "SAM 卡选择文件错误";
                break;
            case 0x65:
                msg = "SAM 卡读错误";
                break;
            case 0x66:
                msg = "SAM 卡写错误";
                break;
            case 0x67:
                msg = "SAM 卡认证错误";
                break;
            case 0x68:
                msg = "SAM 卡随机数错误";
                break;
            case 0x69:
                msg = "SAM 卡 DES 计算错误";
                break;
            case 0x6A:
                msg = "SAM 卡生成钱包密钥错误";
                break;
            case 0x71:
                msg = "PKI 卡 RSA 计算错误";
                break;
            case 0x72:
                msg = "PKI 卡 DES 计算错误";
                break;
            case 0x7E:
                msg = "SAM 卡执行 APDU 命令错误";
                break;
            case 0x7F:
                msg = "SAM 卡操作超时";
                break;
            case (byte) 0x80:
                msg = "没有卡";
                break;
            case (byte) 0x81:
                msg = "选择卡片错误";
                break;
            case (byte) 0x82:
                msg = "停用卡片错误";
                break;
            case (byte) 0x83:
                msg = "认证卡片错误";
                break;
            case (byte) 0x84:
                msg = "卡片读操作错误";
                break;
            case (byte) 0x85:
                msg = "卡片写操作错误";
                break;
            case (byte) 0x86:
                msg = "卡片写操作中途中断";
                break;
            case (byte) 0x90:
                msg = "不是本系统标准的卡片";
                break;
            case (byte) 0x91:
                msg = "卡片超出有效期";
                break;
            case (byte) 0x92:
                msg = "城市代码或应用代码错误";
                break;
            case (byte) 0x93:
                msg = "非法卡";
                break;
            case (byte) 0x94:
                msg = "黑名单卡";
                break;
            case (byte) 0x95:
                msg = "钱包余额不足";
                break;
            case (byte) 0x96:
                msg = "钱包余额超出上限";
                break;
            case (byte) 0x97:
                msg = "钱包未启用";
                break;
            case (byte) 0x98:
                msg = "钱包已停用";
                break;
            case (byte) 0x99:
                msg = "钱包正本被破坏";
                break;
            case (byte) 0x9A:
                msg = "钱包已停用";
                break;
            case (byte) 0x9F:
                msg = "公共信息区被破坏";
                break;
            case (byte) 0xAF:
                msg = "卡片操作超时";
                break;
            case (byte) 0xB0:
                msg = "交易操作中途中断";
                break;
            case (byte) 0xB1:
                msg = "交易中断";
                break;
            case (byte) 0xB2:
                msg = "前一步指令未执行或执行失败";
                break;
            case (byte) 0xC1:
                msg = "联机充值请求被拒绝";
                break;
            case (byte) 0xC2:
                msg = "联机充值认证失败";
                break;
            case (byte) 0xC3:
                msg = "交易结果提交错误";
                break;
            case (byte) 0xCE:
                msg = "联机充值协议错误";
                break;
            case (byte) 0xCF:
                msg = "交易操作超时";
                break;
            case (byte) 0xD0:
                msg = "远程读写器执行错";
                break;
            case (byte) 0xE0:
                msg = "MIFARE 硬件初始化错误";
                break;
            case (byte) 0xE1:
                msg = "SAM 硬件初始化错误";
                break;
            case (byte) 0xE2:
                msg = "命令错误";
                break;
            case (byte) 0xE3:
                msg = "参数错误";
                break;
            case (byte) 0xE4:
                msg = "检验和错误";
                break;
            case (byte) 0xE5:
                msg = "线路通讯超时";
                break;
            case (byte) 0xE6:
                msg = "内部 FLASH 写错误";
                break;
            case (byte) 0x30:
                msg = "报文头错";
                break;
            case (byte) 0x31:
                msg = "卡片不一致";
                break;
            case (byte) 0x32:
                msg = "流水号不一致";
                break;
            case (byte) 0x33:
                msg = "MAC 错";
                break;
            default:
                msg = "未知错误";
        }

//        ToastUtil.showWrning(context, msg);
        NoticeDialog noticeDialog = new NoticeDialog(context, msg, null);
        noticeDialog.show();
    }


    /**
     0x00  MOC_ERROR_NORMAL  操作正常
     0x60  MOC_ERROR_SAM_EMPTY  没有安装 SAM 卡
     0x61  MOC_ERROR_SAM_INIT  SAM 卡初始化错误或未初始化
     0x62  MOC_ERROR_SAM_PIN  SAM 卡检验 PIN 错误
     0x63  MOC_ERROR_SAM_TYPE  SAM 卡类型与交易类型不匹配
     0x64  MOC_ERROR_SAM_SELECT  SAM 卡选择文件错误
     0x65  MOC_ERROR_SAM_READ  SAM 卡读错误
     0x66  MOC_ERROR_SAM_WRITE  SAM 卡写错误
     0x67  MOC_ERROR_SAM_AUTH  SAM 卡认证错误
     0x68  MOC_ERROR_SAM_CHALLENGE  SAM 卡随机数错误
     0x69  MOC_ERROR_SAM_DES  SAM 卡 DES 计算错误
     0x6A  MOC_ERROR_SAM_CREATE_KEY  SAM 卡生成钱包密钥错误
     0x71  MOC_ERROR_PKI_RSA  PKI 卡 RSA 计算错误
     0x72  MOC_ERROR_PKI_DES  PKI 卡 DES 计算错误
     0x7E  MOC_ERROR_SAM_APDU  SAM 卡执行 APDU 命令错误
     0x7F  MOC_ERROR_SAM_TIMEOUT  SAM 卡操作超时
     0x80  MOC_ERROR_CARD_NOTAG  没有卡
     0x81  MOC_ERROR_CARD_SELECT  选择卡片错误
     0x82  MOC_ERROR_CARD_HALT  停用卡片错误
     0x83  MOC_ERROR_CARD_AUTH  认证卡片错误
     0x84  MOC_ERROR_CARD_READ  卡片读操作错误
     0x85  MOC_ERROR_CARD_WRITE  卡片写操作错误
     0x86  MOC_ERROR_CARD_PENDING  卡片写操作中途中断
     0x87  MOC_ERROR_CARD_NORESP  充值卡片无响应
     0x90  MOC_ERROR_CARD_UNKOWN  不是本系统标准的卡片
     0x91  MOC_ERROR_CARD_OUTOFDATE  卡片超出有效期
     0x92  MOC_ERROR_CARD_CITY  城市代码或应用代码错误
     0x93  MOC_ERROR_CARD_INVALID  非法卡
     0x94  MOC_ERROR_CARD_NAMELIST  黑名单卡
     0x95  MOC_ERROR_CARD_OWEPAY  钱包余额不足
     0x96  MOC_ERROR_CARD_OVERLIMIT  钱包余额超出上限
     0x97  MOC_ERROR_CARD_PURSE_UNUSED  钱包未启用
     0x98  MOC_ERROR_CARD_PURSE_STOPPED 钱包已停用
     0x99  MOC_ERROR_CARD_PURSE_FAIL  钱包正本被破坏
     0x9A  MOC_ERROR_CARD_PURSE_INUSE  钱包已停用
     0x9F  MOC_ERROR_CARD_MUTEX_FAIL  公共信息区被破坏
     0xAF  MOC_ERROR_CARD_TIMEOUT  卡片操作超时
     0xB0  MOC_ERROR_TS_PENDING  交易操作中途中断
     0xB1  MOC_ERROR_TS_FAIL  交易中断
     0xB2  MOC_ERROR_TS_STEP  前一步指令未执行或执行失败
     0xC1  MOC_ERROR_ONTS_REFUSED  联机充值请求被拒绝
     0xC2  MOC_ERROR_ONTS_AUTH  联机充值认证失败
     0xC3  MOC_ERROR_CARD_COMMIT  交易结果提交错误
     0xCE  MOC_ERROR_CARD_PROTOCOL  联机充值协议错误
     0xCF  MOC_ERROR_CARD_TS_TIMEOUT  交易操作超时
     0xD0  MOC_ERROR_CARD_HOST_CTL  远程读写器执行错
     0xE0  MOC_ERROR_MIFARE_INIT  MIFARE 硬件初始化错误
     0xE1  MOC_ERROR_ICC_INIT  SAM 硬件初始化错误
     0xE2  MOC_ERROR_INVALID_COMMAND  命令错误
     0xE3  MOC_ERROR_INVALID_PARAM  参数错误
     0xE4  MOC_ERROR_CHECKSUM  检验和错误
     0xE5  MOC_ERROR_COMM_TIMEOUT  线路通讯超时
     0xE6  MOC_ERROR_FLASH_WRITE  内部 FLASH 写错误
     0x30  PAC_ERROR_HEADER_INVALID  报文头错
     0x31  PAC_ERROR_CARD_CHANGE  卡片不一致
     0x32  PAC_ERROR_SYSSEQU  流水号不一致
     0x33  PAC_ERROR__MAC  MAC 错
     **/
}
