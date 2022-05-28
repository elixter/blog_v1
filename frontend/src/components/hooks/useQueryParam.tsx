import * as queryString from 'querystring';

export const useQueryParam = () => {
	const qs = window.location.search.replace('?', '');
	return queryString.parse(qs);
};

export default useQueryParam;
