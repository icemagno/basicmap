from tensorflow/tensorflow
LABEL maintainer "Carlos M. Abreu <magno.mabreu@gmail.com>"

USER root
RUN apt update && apt -y upgrade && mkdir /home/solon && apt install -y vim openjdk-11-jdk
	
RUN pip3 install --upgrade \
	pip \
	nltk \
	fire \
	tflearn \
	flask \
	transformers \
	tensorflow \
	gpt_2_simple

WORKDIR /home/solon
EXPOSE 80
EXPOSE 8080
EXPOSE 8008

COPY ./target/solon-1.0.war /opt/
COPY ./server.py /opt/
COPY ./chatbot/* /opt/

COPY ./demodata/* /home/solon/
COPY ./start.sh /opt/


RUN chmod 777 /opt/*.sh && chmod 777 /opt/*.py

RUN python3 /opt/prepare.py

ENTRYPOINT ["/opt/start.sh"]
