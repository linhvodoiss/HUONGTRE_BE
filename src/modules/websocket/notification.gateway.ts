import {
  WebSocketGateway,
  WebSocketServer,
  SubscribeMessage,
  OnGatewayInit,
  OnGatewayConnection,
  OnGatewayDisconnect,
} from '@nestjs/websockets';
import { Logger } from '@nestjs/common';
import { Server, Socket } from 'socket.io';

@WebSocketGateway({
  cors: {
    origin: '*',
  },
  namespace: 'notifications', // Tương đương với `/topic` prefixes trong Spring
})
export class NotificationGateway
  implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect
{
  @WebSocketServer() server: Server;
  private logger: Logger = new Logger('NotificationGateway');

  afterInit(server: Server) {
    this.logger.log('WebSocket Gateway Initialized');
  }

  handleConnection(client: Socket, ...args: any[]) {
    this.logger.log(`Client connected: ${client.id}`);
  }

  handleDisconnect(client: Socket) {
    this.logger.log(`Client disconnected: ${client.id}`);
  }

  // Thay thế cho @MessageMapping trong Spring Boot
  @SubscribeMessage('subscribe_orders')
  handleOrderSubscription(client: Socket, payload: any): void {
    client.join('orders_room');
    client.emit('subscribed', { status: 'success', room: 'orders_room' });
  }

  // Hàm helper để gửi thông báo từ Service khác
  sendNewOrderNotification(order: any) {
    this.server.to('orders_room').emit('new_order', order);
  }
}
