import { ParsedUrlQuery } from 'querystring';
import useQueryParam from '../useQueryParam';

const usePostFilter = (params: ParsedUrlQuery) => {
	let filterString: string | undefined = '';
	let filterType: string | undefined = '';

	Object.keys(params).forEach((key) => {
		if (key === 'category' || key === 'hashtag') {
			filterType = key as string | undefined;
			filterString = params[key] as string | undefined;
		}
	});

	return { filterType, filterString };
};

export default usePostFilter;
