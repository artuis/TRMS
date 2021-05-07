# TRMS
## Tuition Reimbursement Manaagement System
The Tuition Reimbursement System, TRMS, allows users to submit reimbursements for courses and training. The submitted reimbursement must be approved by that employee's supervisor, department head, and benefits coordinator. The benefits coordinator then reviews the grade received before finalizing the reimbursement.
## Technologies Used
* Java 8
* Maven
* Log4j2
* JUnit 2
* Mockito
* Javalin
* Postman
* DataStax
* AWS Keyspaces
* AWS S3
## Features
* Submitting reimbursement requests
* Denying or requesting more information for reimbursment requests
* Attachments upload for a specific form
## Getting Started/Usage
1. Java 8 Runtime Environment installed
2. AWS Keyspaces and S3 access.
With those complete you can clone this repository onto your local machine:

```git clone git@github.com:artuis/TRMS.git```

Then set environment variables for AWS_USER and AWS_PASS using your AWS Keyspaces credentials and S3 credentials in your local folder and run:

```mvn build```

To generate a .jar file to run.
## Contributors
Thomas An
