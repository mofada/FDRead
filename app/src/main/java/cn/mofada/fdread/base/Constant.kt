/**
 * Copyright 2016 JustWayward Team
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.mofada.fdread.base

/**
 * @author yuyh.
 * *
 * @date 16/8/5.
 */
class Constant {
    companion object {
        //动画名称
        var INDICATOR: String = "INDICATOR"

        //广播-登录
        var ACTION_LOGININ: String = "ACTION_LOGININ"
        //广播-登出
        var ACTION_LOGINOUT: String = "ACTION_LOGINOUT"
        //广播-书籍添加
        var ACTION_BOOKADD: String = "ACTION_BOOKADD"

        //广播参数
        var USER_ICONURL: String = "iconurl"
        var USER_NAME: String = "name"
        var USER_UID: String = "uid"

        //SharedPreference存储键值
        var PREFERS_UID: String = "uid"
        var PREFERS_NAME: String = "name"
        var PREFERS_ICONURL: String = "iconurl"
        var PREFERS_ISLOGIN: String = "is_login"
        var PREFERS_STORE: String = "PREFERS_STORE"
        var PREFERS_NOTIFICATION: String = "PREFERS_NOTIFICATION"
        var PREFERS_DISCUSS: String = "PREFERS_DISCUSS"
        var PREFERS_RANKING: String = "PREFERS_RANKING"
        var PREFERS_CHANNEL_1: String = "PREFERS_CHANNEL_1"
        var PREFERS_CHANNEL_2: String = "PREFERS_CHANNEL_2"
        var PREFERS_CHANNEL_3: String = "PREFERS_CHANNEL_3"
        var PREFERS_CHANNEL_4: String = "PREFERS_CHANNEL_4"
        var PREFERS_CHANNEL_5: String = "PREFERS_CHANNEL_5"
        var PREFERS_CHANNEL_6: String = "PREFERS_CHANNEL_6"
        var PREFERS_CHANNEL_7: String = "PREFERS_CHANNEL_7"
        var PREFERS_CHANNEL_8: String = "PREFERS_CHANNEL_8"

        //字体大小
        var READ_TEXT_SIZE: String = "textSize"

        //背景色
        var READ_BACKGROUND: String = "background"

        //通知id
        var NOTIFICATION_ID: Int = 0xfd

        fun getChannel(type: Int): String = "PREFERS_CHANNEL_$type"
    }
}
