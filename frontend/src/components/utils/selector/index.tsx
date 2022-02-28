import { memo, PropsWithChildren, useEffect, useRef, useState } from 'react';

type Props = {
	children: PropsWithChildren<any>;
	defaultValue: any;
	onChange: any;
};

const Selector = function ({ children, defaultValue, onChange }: Props) {
	const selectRef = useRef<HTMLSelectElement>(null);

	return (
		<div className="selector-wrapper">
			<select defaultValue={defaultValue} onChange={onChange} ref={selectRef}>
				{children}
			</select>
		</div>
	);
};

export default memo(Selector, (prevProps, nextProps) => prevProps.defaultValue === nextProps.defaultValue);
