import http from 'k6/http';
import { sleep } from 'k6';

const VIRTUAL_USERS = process.env.VIRTUAL_USERS ?? 10;
const TEST_DURATION = process.env.TEST_DURATION ?? '30s';

export const options = {
    vus: VIRTUAL_USERS,
    duration: TEST_DURATION,
};

export default function () {
    http.post('http://creditapplication-service.lpt.svc.cluster.local:8080/api/credit-application');
    sleep(1);
}