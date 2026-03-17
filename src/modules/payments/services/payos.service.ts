import { Injectable, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
const PayOS = require('@payos/node');

@Injectable()
export class PayOSService {
  private readonly logger = new Logger(PayOSService.name);
  private payos: any;

  constructor(private configService: ConfigService) {
    this.payos = new PayOS(
      this.configService.get('PAYOS_CLIENT_ID'),
      this.configService.get('PAYOS_API_KEY'),
      this.configService.get('PAYOS_CHECKSUM_KEY'),
    );
  }

  async createPaymentLink(amount: number, orderCode: number, description: string) {
    const returnUrl = this.configService.get('FRONTEND_URL') + '/payment/success';
    const cancelUrl = this.configService.get('FRONTEND_URL') + '/payment/cancel';

    const body = {
      orderCode,
      amount,
      description,
      returnUrl,
      cancelUrl,
    };

    try {
      return await this.payos.createPaymentLink(body);
    } catch (error) {
      this.logger.error('Error creating payment link', error);
      throw error;
    }
  }

  verifyWebhookData(webhookBody: any) {
    return this.payos.verifyPaymentData(webhookBody);
  }
}
