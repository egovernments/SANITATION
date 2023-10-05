import React,{Fragment} from 'react'
import { Card,Header,CardSubHeader,CardHeader,MultiLink,ShareIcon,ArrowDownward,ArrowUpward } from '@egovernments/digit-ui-react-components'


const VerticalLine = () => {
  return (
    <div className="vertical-line"></div>
  );
}

const Compliance = ({t}) => {

  return(
    <div className='performance-item'>
      <div className='performance-item-percentage'>3%</div>
      <div className='performance-item-label'>Test Comdivliance</div>
      <div className='performance-item-label-caption performance-item-label-caption__fail'> <ArrowDownward />10% than State average</div>
    </div>
  );
}

const Output = ({t}) => {

  return (
    <div className='performance-item'>
      <span className='performance-item-sla performance-item-sla__pass'>Pass</span>
      <div className='performance-item-label'>Last Output Quality</div>
      <div className='performance-item-label-caption'>as on 17/04/2023(Lab)</div>
    </div>
  )
}

const YourPerformance = ({t}) => {
  return (
    <div>
      <div className='performance-header'>
        <Header styles={{fontSize:"26px",marginBottom:"0px",marginLeft:"0px"}}>Your Performance</Header>
        <MultiLink
          className="multilink-block-wrapper multilink-label "
          label={t(`TQM_SHARE`)}
          icon={<ShareIcon className="mrsm" fill="#f18f5e" />}
          onHeadClick={()=>{}}
        />
      </div>
      <Card className="performance-container">
        <Compliance t={t} />
        <VerticalLine />
        <Output t={t}/>
      </Card>
    </div>
  )
}

export default YourPerformance