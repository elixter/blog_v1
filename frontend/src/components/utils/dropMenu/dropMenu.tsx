import React from 'react';

type Props = {
	open: boolean;
	children: any;
};

const DropMenu = function ({ open, children }: Props) {
	return (
		<div
			className={`btns-group ${(open && 'active') || ''} js-more`}
			style={{
				zIndex: 100,
			}}
		>
			{children}
		</div>
	);
};

export default DropMenu;
