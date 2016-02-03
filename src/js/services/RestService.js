/*global $ */

import when from 'when';
import LoginStore from '../stores/LoginStore';
import AuthenService from '../services/AuthenService';

class RestService {

    get(url) {
        return when(
            $.ajax({
                url: url,
                dataType: 'json',
                cache: false,
                headers: {
                    'Authorization': 'bearer ' + LoginStore.token
                }
            })
        ).catch(function (e) {
            if (e.status === 401) {
                AuthenService.logout();
            }
        });
    }

    post(url, data) {
        return when(
            $.ajax({
                url: url,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                method: 'POST',
                cache: false,
                headers: {
                    'Authorization': 'bearer ' + LoginStore.token
                },
                data: JSON.stringify(data)
            })
        ).catch(function (e) {
            if (e.status === 401) {
                AuthenService.logout();
            }
        });
    }

    coverUpload(data) {
        return when($.ajax({
                url: '/api/fileupload',
                data: data,
                processData: false,
                contentType: false,
                type: 'POST',
                headers: {
                    'Authorization': 'bearer ' + LoginStore.token
                }
            })
        ).catch(function (e) {
            if (e.status === 401) {
                AuthenService.logout();
            }
        });
    }
}

export default new RestService();