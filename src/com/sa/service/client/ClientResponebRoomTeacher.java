/**
 *
 * 项目名称:[NettyServer]
 * 包:	 [com.sa.service.client]
 * 类名称: [ClientResponebRoomUser]
 * 类描述: [一句话描述该类的功能]
 * 创建人: [Y.P]
 * 创建时间:[2017年7月4日 下午5:00:53]
 * 修改人: [Y.P]
 * 修改时间:[2017年7月4日 下午5:00:53]
 * 修改备注:[说明本次修改内容]
 * 版本:	 [v1.0]
 *
 */
package com.sa.service.client;

import com.sa.net.Packet;
import com.sa.net.PacketHeadInfo;
import com.sa.net.PacketType;

public class ClientResponebRoomTeacher extends Packet {

	public ClientResponebRoomTeacher() {
	}

	public ClientResponebRoomTeacher(PacketHeadInfo packetHead) {
		this.setPacketHead(packetHead);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.ClientResponebRoomTeacher;
	}

	@Override
	public void execPacket() {
	}
}
