type Props = {
	children: any;
};

const Grid = function ({ children }: Props) {
	return <div className="grid-container">{children}</div>;
};

export default Grid;
