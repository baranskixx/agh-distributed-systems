import pika
import random
import sys
import time

QUEUES = ['PT', 'FS', 'SP']

MESSAGES = {
    'PT': '{}: Transport ludzi',
    'FS': '{}: Przewóz towarów',
    'SP': '{}: Instalacja satelity',
}

LOG = '{}: {}'


def main():
    # establish connection
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()
    
    if len(sys.argv) != 2:
        print('Invalid number of arguments!')
        sys.exit(-1)

    agency_signature = sys.argv[1]

    for queue in QUEUES:
        channel.queue_declare(queue=queue)

    message_id = 1
    while True:
        queue_choice = random.choice(QUEUES)
        print(MESSAGES[queue_choice].format(message_id))
        channel.basic_publish(exchange='', routing_key=queue_choice, body=agency_signature + str(message_id))
        message_id += 1
        time.sleep(1)

if __name__ == '__main__':
    main()
