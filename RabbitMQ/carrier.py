import pika
import sys
import time
from datetime import datetime

PREFIXES = {
    'PT': 'PublicTransport',
    'FS': 'FreightService',
    'SPS': 'SatelitePlacementService'
}

DATETIME_FORMAT = "%Y-%m-%d %H:%M:%S"


def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()

    args = sys.argv

    def callbackPublicTransport(ch, method, properties, body):
        print("{}|Transport ludzi, id: {}".format(str(datetime.now().strftime(DATETIME_FORMAT)), str(body)[2:-1]))
        time.sleep(3)
        channel.basic_ack(method.delivery_tag)

    def callbackFreightService(ch, method, properties, body):
        print("{}|Przewóz towarów, id: {}".format(str(datetime.now().strftime(DATETIME_FORMAT)), str(body)[2:-1]))
        time.sleep(3)
        channel.basic_ack(method.delivery_tag)

    def callbackSatelitePlacement(ch, method, properties, body):
        print("{}|Instalacja satelity, id: {}".format(str(datetime.now().strftime(DATETIME_FORMAT)), str(body)[2:-1]))
        time.sleep(3)
        channel.basic_ack(method.delivery_tag)

    if args[1] != '1':
        channel.queue_declare(queue='PT')
        channel.basic_consume(queue='PT', on_message_callback=callbackPublicTransport, auto_ack=False)
    if args[1] != '2':
        channel.queue_declare(queue='FS')
        channel.basic_consume(queue='FS', on_message_callback=callbackFreightService, auto_ack=False)
    if args[1] != '3':
        channel.queue_declare(queue='SP')
        channel.basic_consume(queue='SP', on_message_callback=callbackSatelitePlacement, auto_ack=False)

    print(' [*] Oczekiwanie na zlecenia...')
    channel.start_consuming()

if __name__ == '__main__':
    main()