/**
 *
 * 项目名称:[NettyServer]
 * 包:	 [com.sa.service.client]
 * 类名称: [CUniqueLogon]
 * 类描述: [房间用户列表 下行]
 * 创建人: [Y.P]
 * 创建时间:[2017年7月17日 下午3:10:28]
 * 修改人: [Y.P]
 * 修改时间:[2017年7月17日 下午3:10:28]
 * 修改备注:[说明本次修改内容]
 * 版本:	 [v1.0]
 *
 */
package com.sa.service.client;

import java.util.HashSet;

import com.sa.base.ConfManager;
import com.sa.base.ServerDataPool;
import com.sa.base.ServerManager;
import com.sa.base.element.ChannelExtend;
import com.sa.net.Packet;
import com.sa.net.PacketHeadInfo;
import com.sa.net.PacketType;
import com.sa.util.Constant;

import io.netty.channel.ChannelHandlerContext;

public class CUniqueLogon extends Packet {
	public CUniqueLogon(){}

	public CUniqueLogon(int transactionId, String roomId, String userId, int code) {
		super(transactionId, roomId, "0", userId, code);
	}
	
	public CUniqueLogon(PacketHeadInfo packetHead) {
		this.setPacketHead(packetHead);
	}

	@Override
	public void execPacket() {
		try {
			/** 发消息给发信人*/
			//ServerManager.INSTANCE.sendPacketTo(cl, context, Constant.CONSOLE_CODE_S);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*@Override
	public void execPacket() {
		*//** 如果目标地址是中心地址*//*
		if (this.getRemoteIp().equals(ConfManager.getCenterIp())) {
			*//** 从临时缓存中获取发信人通道*//*
			ChannelHandlerContext context = ServerDataPool.TEMP_CONN_MAP2.get(this.getFromUserId());
			*//** 删除临时缓存*//*
			ServerDataPool.TEMP_CONN_MAP2.remove(this.getToUserId());
			ChannelExtend ce = ServerDataPool.TEMP_CONN_MAP.remove(context);
			*//** 如果状态为0*//*
			if (this.getStatus() ==0) {
				*//** 获取用户角色*//*
				HashSet<String> userRole = toRole((String) this.getOption(2));
				*//** 用户注册*//*
				ServerManager.INSTANCE.addOnlineContext(this.getRoomId(),
						this.getFromUserId(), (String) this.getOption(3),
						(String) this.getOption(4), userRole,
						ConfManager.getTalkEnable(), context, ce.getChannelType());
				*//** 实例化房间用户列表 并 赋值 并 执行*//*
				ClientResponebRoomUser crru = new ClientResponebRoomUser(this.getPacketHead());
				crru.setRemoteIp(this.getRemoteIp());
				crru.setOption(11, "{\"id\":\""+this.getFromUserId()+"\",\"name\":\""+this.getOption(3)+"\",\"icon\":\""+this.getOption(4)+"\",\"role\":[\""+this.getOption(2)+"\"]}");

				crru.execPacket();
			}
			*//** 实例化登录信息 下行*//*
			ClientLogin cl = new ClientLogin(this.getPacketHead(), this.getOptions());
			try {
				*//** 发消息给发信人*//*
				ServerManager.INSTANCE.sendPacketTo(cl, context, Constant.CONSOLE_CODE_S);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

	private HashSet<String> toRole(String role) {
		HashSet<String> userRole = new HashSet<String>();

		if ("1".equals(role)) {
			userRole.add(Constant.ROLE_TEACHER);
		} else if ("2".equals(role)) {
			userRole.add(Constant.ROLE_ASSISTANT);
		} else if ("3".equals(role)) {
			userRole.add(Constant.ROLE_STUDENT);
		} else if ("4".equals(role)) {
			userRole.add(Constant.ROLE_AUDIENCE);
		} else if ("5".equals(role)) {
			
		} else if ("0".equals(role)) {
			userRole.add(Constant.ROLE_PARENT_TEACHER);
		} else {
			userRole.add(role);
		}

		return userRole;
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.CUniqueLogon;
	}

}
