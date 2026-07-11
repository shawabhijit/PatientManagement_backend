# PatientManagement_backend
Production-Ready Patient Management System with Microservices: Java Spring Boot AWS

# Things Have to Learn 
-> GRPC - learn how grpc works like in what format and when to use it.
-> .proto - learn the syntax about .proto file and what the use case of it.

-> API Gateway

-> Deep drive into kafka producers and consumers like how persistence work , read about kafka 
high throughput , real time processing , fault tolerance then learn about how partitioning
works in kafka , Brokers and clusters and at last Zookeeper Integration

## Overall Startup Sequence

                Kafka Starts
                    │
                    ▼
                Controller Starts
                    │
                    ▼
                Raft Initializes (KRaft Mode)
                    │
                    ▼
                Leader Election
                    │
                    ▼
                Metadata Loaded
                    │
                    ▼
                Broker Starts
                    │
                    ▼
                Broker Registers
                    │
                    ▼
                Topics/Logs Loaded
                    │
                    ▼
                Group Coordinator Starts
                    │
                    ▼
                Transaction Coordinator Starts
                    │
                    ▼
                Socket Listeners Open (9092/9093/9094)
                    │
                    ▼
                Kafka Ready for Producers & Consumers