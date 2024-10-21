import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '30s',
};

export default function () {
    http.post('http://creditapplication-service.lpt.svc.cluster.local:8080/api/credit-application');
    sleep(1);
}