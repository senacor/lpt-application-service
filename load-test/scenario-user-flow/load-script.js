import http from 'k6/http';
import { Rate } from 'k6/metrics';
import { sleep } from 'k6';

const creditApplicationCreationRate = new Rate('failed credit application creation');
const creditApplicationAcceptanceRate = new Rate('failed credit application acceptance');

export const options = {
    vus: 10,
    duration: '30s',
    thresholds: {
        'failed credit application creation': ['rate<0.1'],
        'failed credit application acceptance': ['rate<0.1'],
        'http_req_duration': ['p(95)<400'],
    },
};

export default function () {
    const creditApplicationCreation = http.post('http://creditapplication-service.lpt.svc.cluster.local:8080/api/credit-applications', {
        "creditAmount":10000.00,
        "firstName":"Harry",
        "lastName":"Potter",
        "zipCode":"12345",
        "occupation":"IT",
        "monthlyNetIncome":3000.00,
        "monthlyExpenses":2500.00
    });
    creditApplicationCreationRate.add(creditApplicationCreation.status !== 201);
    const applicationId = creditApplicationCreation.body.id;
    // after receiving the credit decision, the customer decides within 5 seconds to accept or decline the credit application
    sleep(5);
    const creditApplicationAcceptance = http.post(`http://creditapplication-service.lpt.svc.cluster.local:8080/api/credit-applications/${applicationId}`, {});
    creditApplicationAcceptanceRate.add(creditApplicationAcceptance.status !== 200);
}