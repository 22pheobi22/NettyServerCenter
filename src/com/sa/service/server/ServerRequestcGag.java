/**
 *
 * 项目名称:[NettyServer]
 * 包:	 [com.sa.service.server]
 * 类名称: [ServerRequestcGag]
 * 类描述: [一句话描述该类的功能]
 * 创建人: [Y.P]
 * 创建时间:[2017年7月4日 下午5:00:16]
 * 修改人: [Y.P]
 * 修改时间:[2017年7月4日 下午5:00:16]
 * 修改备注:[说明本次修改内容]
 * 版本:	 [v1.0]
 *
 */
package com.sa.service.server;

import java.util.HashMap;
import java.util.Map;

import com.sa.base.ConfManager;
import com.sa.base.ServerDataPool;
import com.sa.base.ServerManager;
import com.sa.base.element.People;
import com.sa.net.Packet;
import com.sa.net.PacketType;
import com.sa.service.client.ClientMsgReceipt;
import com.sa.service.permission.Permission;
import com.sa.util.Constant;

public class ServerRequestcGag extends Packet {
	public ServerRequestcGag(){}

	@Override
	public PacketType getPacketType() {
		return PacketType.ServerRequestcGag;
	}

	@Override
	public void execPacket() {
		/** 校验用户角色*/
		Map<String, Object> result = Permission.INSTANCE.checkUserRole(this.getRoomId(), this.getFromUserId(), Constant.ROLE_TEACHER);
		/** 如果校验成功*/
		if (0 == ((Integer) result.get("code"))) {
			/** 根据房间id和目标用户id获取用户信息*/
			String userId= this.getToUserId();
			People people =ServerDataPool.serverDataManager.notSpeakAuth(this.getRoomId(), this.getToUserId());
			/** 如果人员信息为空*/
			if (null != people) {
				Map<String, Object> result2 = new HashMap<>();

				result2.put("code", 10095);
				result2.put("msg", Constant.ERR_CODE_10095);
				/** 实例化消息回执 并 赋值 并 执行*/
				ClientMsgReceipt clientMsgReceipt = new ClientMsgReceipt(this.getPacketHead(), result2);
				clientMsgReceipt.setToUserId(userId);
				clientMsgReceipt.execPacket();
			}

			if (ConfManager.getIsCenter() && !ConfManager.getCenterIp().equals(this.getRemoteIp())) {
				if (null != people) {
					this.setStatus(10095);
				}

				this.setToUserId(userId);
				/** 转发到中心*/
				ServerManager.INSTANCE.sendPacketToCenter(this, Constant.CONSOLE_CODE_TS);
			}
		}
		/** 实例化消息回执 并 赋值 并 执行*/
		new ClientMsgReceipt(this.getPacketHead(), result).execPacket();
	}
}
